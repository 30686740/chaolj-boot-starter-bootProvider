package com.chaolj.core.bootUtils.bootConfig;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "knife4j.my")
public class Knife4jProperties {
    private String description = "myapp";
    private String termsOfServiceUrl = "http://www.chaolj.com/";
    private String version = "1.0";
    private String groupName = "myapp project";
    private String basePackage = "com.chaolj.api";
    private String defclient = "001";
    private String deftoken = "dev";
}
