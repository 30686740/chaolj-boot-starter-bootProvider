package com.chaolj.core.bootUtils.bootConfig;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.chaolj.core.MyApp;
import com.chaolj.core.MyConst;
import com.chaolj.core.MyUser;
import com.chaolj.core.bootUtils.bootAnnotation.TokenTrackIgnore;
import com.chaolj.core.commonUtils.myDto.DataResultDto;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class TokenTrackInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 判断是否方法请求
        if (!handler.getClass().isAssignableFrom(HandlerMethod.class)) return true;

        // 判断方法上是否注解 TokenIgnore
        var handlerMethod = (HandlerMethod) handler;
        if (handlerMethod.getMethodAnnotation(TokenTrackIgnore.class) != null) return true;

        // 判断类上是否注解 TokenIgnore
        if (handlerMethod.getMethod().getDeclaringClass().getAnnotation(TokenTrackIgnore.class) != null) return true;

        try {
            var token = request.getHeader(MyConst.HEADERKEY_TOKEN);
            var values = MyApp.Server().TokenServer().DecryptToken(token, true);
            MyUser.setCurrentUserToken(token);
            MyUser.setCurrentUserClient(values.get(0));
            MyUser.setCurrentUserName(values.get(1));

            return true;
        } catch (Exception ex) {
            this.OutputInvalid(response, ex.getMessage());
            return false;
        }
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        MyUser.Remove();
    }

    private void OutputInvalid(HttpServletResponse response, String errmessage) {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");

        var responseData = DataResultDto.Empty();
        responseData.setResult(false);
        responseData.setCode("401");
        responseData.setMessage(errmessage);

        var responseContent = JSONObject.toJSONString(responseData, SerializerFeature.WriteMapNullValue);

        try {
            var pw = response.getWriter();
            pw.flush();
            pw.println(responseContent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
