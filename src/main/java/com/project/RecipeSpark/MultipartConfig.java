package com.project.RecipeSpark;

import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.unit.DataSize;

import jakarta.servlet.MultipartConfigElement;

@Configuration
public class MultipartConfig {

    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setLocation(System.getProperty("user.dir") + "/tmp");
        factory.setMaxFileSize(DataSize.ofMegabytes(10)); // 최대 파일 크기
        factory.setMaxRequestSize(DataSize.ofMegabytes(50)); // 최대 요청 크기
        return factory.createMultipartConfig();
    }
}