package com.solux.piccountbe.config.image;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${upload.dir}")
    private String uploadDir;

//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/api/**")
//                .allowedOrigins("http://localhost:5179")
//                .allowedMethods("GET", "POST", "DELETE", "OPTIONS", "PUT", "PATCH")
//                .allowedHeaders("*")
//                .allowCredentials(true);
//    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        registry.addResourceHandler("/images/profile/**")
                .addResourceLocations("classpath:/static/images/profile/");

        registry.addResourceHandler("/images/profileImages/**")
                .addResourceLocations("file:" + uploadDir + "/");
    }
}