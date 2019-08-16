package com.jtalics.bootes;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ConfigMvc implements WebMvcConfigurer {

    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/dashboard").setViewName("dashboard");
        registry.addViewController("/toss").setViewName("toss");
        registry.addViewController("/confirm").setViewName("confirm");
        registry.addViewController("/success").setViewName("success");
        registry.addViewController("/failure").setViewName("failure");
        registry.addViewController("/help").setViewName("help");
        registry.addViewController("/spammer").setViewName("spammer");
        registry.addViewController("/login").setViewName("login");
    }
}