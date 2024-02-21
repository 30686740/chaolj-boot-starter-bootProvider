package com.chaolj.core.bootUtils.bootConfig;

import com.chaolj.core.MyConst;
import com.chaolj.core.commonUtils.myServer.MyServerTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.*;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;
import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableSwagger2WebMvc
@EnableConfigurationProperties(Knife4jProperties.class)
public class Knife4jConfiguration {
    @Autowired
    Knife4jProperties _Properties;

    @Autowired(required = false)
    MyServerTemplate _MyServerTemplate;

    @Bean(value = "defaultApi2")
    public Docket defaultApi2() {
        var apiInfoBuilder = new ApiInfoBuilder()
                .description(this._Properties.getDescription())
                .termsOfServiceUrl(this._Properties.getTermsOfServiceUrl())
                .version(this._Properties.getVersion())
                .build();

        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfoBuilder)
                .globalOperationParameters(getParameterList())
                .groupName(this._Properties.getGroupName())
                .select()
                .apis(RequestHandlerSelectors.basePackage(this._Properties.getBasePackage()))
                .paths(PathSelectors.any())
                .build();
    }

    private List<Parameter> getParameterList() {
        var defaultToken = this._MyServerTemplate.TokenServer().EncryptToken(this._Properties.getDefclient(), this._Properties.getDeftoken(), 3650);

        var pb = new ParameterBuilder();
        pb.name(MyConst.HEADERKEY_TOKEN)
                .description(MyConst.HEADERKEY_TOKEN)
                .modelRef(new ModelRef("String"))
                .parameterType("header")
                .defaultValue(defaultToken)
                .required(true).build();

        var pars = new ArrayList<Parameter>();
        pars.add(pb.build());

        return pars;
    }
}
