package com.mx.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class MyWebConfigureAdapter extends WebMvcConfigurerAdapter{

    public void addResourceHandlers(ResourceHandlerRegistry registry){
        registry.addResourceHandler("/my/**").addResourceLocations("file:/webapp/WEB-INF/page/");
        super.addResourceHandlers(registry);
    }
}
