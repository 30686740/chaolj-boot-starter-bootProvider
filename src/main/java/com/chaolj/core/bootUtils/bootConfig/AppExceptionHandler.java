package com.chaolj.core.bootUtils.bootConfig;

import com.chaolj.core.MyUser;
import lombok.extern.slf4j.Slf4j;
import org.codehaus.groovy.syntax.TokenException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.security.auth.message.AuthException;
import javax.servlet.http.HttpServletRequest;
import com.chaolj.core.commonUtils.myDto.UIException;
import com.chaolj.core.commonUtils.myDto.DataResultDto;
import com.chaolj.core.MyApp;

@Slf4j
@RestControllerAdvice
public class AppExceptionHandler {
    private void Log(HttpServletRequest request, String message, String stackTrace) {
        StringBuilder sb = new StringBuilder();
        sb.append("发生错误！" + message);
        sb.append(System.lineSeparator() + "requestMethod: " + request.getMethod());
        sb.append(System.lineSeparator() + "requestUrl: " + request.getRequestURI());
        sb.append(System.lineSeparator() + "requestQueryString: " + request.getQueryString());
        sb.append(System.lineSeparator() + "requestBody: " + MyApp.Helper().ServletHelper().GetRequestBody(request));
        sb.append(System.lineSeparator() + "requestIP: " + MyApp.Helper().ServletHelper().GetRequestIp(request));
        sb.append(System.lineSeparator() + "requestUserAgent: " + request.getHeader("User-Agent"));
        sb.append(System.lineSeparator() + "UserToken: " + MyUser.getCurrentUserToken());
        sb.append(System.lineSeparator() + "UserName: " + MyUser.getCurrentUserName());
        sb.append(System.lineSeparator() + "HttpTrackId: " + MyUser.getCurrentHttpTrackId());
        sb.append(System.lineSeparator() + "StackTrace: " + stackTrace);
        log.error(sb.toString());
    }

    @ExceptionHandler(UIException.class)
    public DataResultDto<String> UIExceptionHand(HttpServletRequest request, UIException ex) {
        var dto = DataResultDto.<String>Empty();
        dto.setResult(false);
        dto.setCode("403");
        dto.setMessage(ex.getMessage());

        return dto;
    }

    @ExceptionHandler(TokenException.class)
    public DataResultDto<String> TokenExceptionHand(HttpServletRequest request, TokenException ex) {
        var dto = DataResultDto.<String>Empty();
        dto.setResult(false);
        dto.setCode("401");
        dto.setMessage(ex.getMessage());

        return dto;
    }

    @ExceptionHandler(BadSqlGrammarException.class)
    public DataResultDto<String> SQLSyntaxErrorExceptionHand(HttpServletRequest request, BadSqlGrammarException ex) {
        this.Log(request, ex.getSQLException().getMessage(), ex.getSQLException().toString());

        var dto = DataResultDto.<String>Empty();
        dto.setResult(false);
        dto.setCode(String.valueOf(ex.getSQLException().getErrorCode()));
        dto.setMessage("处理数据异常！" + ex.getSQLException().getMessage());

        return dto;
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public DataResultDto<String> HttpMessageNotReadableExceptionHand(HttpServletRequest request, HttpMessageNotReadableException ex) {
        this.Log(request, ex.getMessage(), ex.toString());

        var dto = DataResultDto.<String>Empty();
        dto.setResult(false);
        dto.setCode("403");
        dto.setMessage("请求数据异常！" + ex.getMessage());

        return dto;
    }

    @ExceptionHandler(NullPointerException.class)
    public DataResultDto<String> NullPointerExceptionHand(HttpServletRequest request, NullPointerException ex) {
        this.Log(request, ex.getMessage(), ex.toString());

        var dto = DataResultDto.<String>Empty();
        dto.setResult(false);
        dto.setCode("500");
        dto.setMessage("空引用异常！" + ex.getMessage());

        return dto;
    }

    @ExceptionHandler(Exception.class)
    public DataResultDto<String> UnkownExceptionHand(HttpServletRequest request, Exception ex) {
        this.Log(request, ex.getMessage(), ex.toString());

        var dto = DataResultDto.<String>Empty();
        dto.setResult(false);
        dto.setCode("500");
        dto.setMessage("服务内部异常！" + ex.getMessage());

        return dto;
    }
}
