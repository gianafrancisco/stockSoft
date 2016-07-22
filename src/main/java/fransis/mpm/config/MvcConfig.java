/*
 * Copyright (C) 2016-2016 Francisco Giana <gianafrancisco@gmail.com>
 *
 */

package fransis.mpm.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by francisco on 6/27/16.
 */
@Configuration
@Component
public class MvcConfig extends WebMvcConfigurerAdapter {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry){
        registry.addResourceHandler("/dev/bower_components/**")
                //.addResourceLocations("file:/home/francisco/git/stockSoft/src/main/gui/bower_components/")
                .addResourceLocations("file:src/main/gui/bower_components/")
                .setCachePeriod(0);
        registry.addResourceHandler("/dev/**")
                //.addResourceLocations("file:/home/francisco/git/stockSoft/src/main/gui/app/")
                .addResourceLocations("file:src/main/gui/app/")
                .setCachePeriod(0);
    }
}