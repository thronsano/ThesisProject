package com.diamondLounge.utility;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/login").setViewName("publicTemplates/login");
        registry.addViewController("/").setViewName("publicTemplates/login");
        registry.addViewController("/home").setViewName("home");
        registry.addViewController("/register").setViewName("publicTemplates/register");
        registry.addViewController("/reset").setViewName("reset");
        registry.addViewController("/forgot").setViewName("forgot");
        registry.addViewController("/settings").setViewName("settings/accountSettings");
    }
}