package com.chaolj.core.bootUtils.bootConfig;

import cn.hutool.core.util.StrUtil;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.sql.Connection;
import java.util.Map;
import java.util.Properties;

// SQL拦截
// 在下面的方法中进行注入
// com.chaolj.storage.dal.config.DataSourceConfig_chaolj_storage.SqlSessionFactory
@Component
@Intercepts({ @Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class}) })
public class SqlInterceptor implements Interceptor {
    public final String QUERY_WHERE_PLACEHOLDER = "where 1 = 1";
    public final String QUERY_WHERE_METHODKEY = "whereSQL";

    @Value("${druid.ui-enabled}")
    private Boolean uiEnabled;

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        // 获取参数
        var statementHandler = (StatementHandler) invocation.getTarget();
        var boundSql = statementHandler.getBoundSql();

        Map<String, Object> params;
        try {
            params = (Map<String, Object>) boundSql.getParameterObject();
        }
        catch (Exception ex){
            return invocation.proceed();
        }

        if (params == null) return invocation.proceed();
        if (params.isEmpty()) return invocation.proceed();
        if (!params.containsKey(this.QUERY_WHERE_METHODKEY)) return invocation.proceed();

        var whereSQLParam = params.get(this.QUERY_WHERE_METHODKEY);
        if (whereSQLParam == null) return invocation.proceed();
        if (!(whereSQLParam instanceof String)) return invocation.proceed();

        var whereSQL = (String) whereSQLParam;
        if (StrUtil.isBlank(whereSQL)) return invocation.proceed();

        // 拼写新 sql
        var oldSQL = boundSql.getSql();
        var newSQL = StrUtil.replace(oldSQL, this.QUERY_WHERE_PLACEHOLDER, whereSQL, true);

        // 回写新 sql
        var field = boundSql.getClass().getDeclaredField("sql");
        field.setAccessible(true);
        field.set(boundSql, newSQL);

        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        return Interceptor.super.plugin(target);
    }

    @Override
    public void setProperties(Properties properties) {
        Interceptor.super.setProperties(properties);
    }
}
