package com.alibaba.citrus.webx.context;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.ServletContext;

/**
 * Created by Congmin Qiu wb-qcm262540
 * Date: 2017/2/24
 * Time: 10:17
 */
public class WebxComponentsLoaderMimic extends ContextLoader {
    private ServletContext servletContext;

    @Override
    public WebApplicationContext initWebApplicationContext(ServletContext context){
        this.servletContext = context;
        init();
        return super.initWebApplicationContext(context);
    }

    // if you want this method to be able to be used by its extend class, decorate it with protected
    protected void init(){
        

    }
}
