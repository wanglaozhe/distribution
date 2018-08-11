package com.yuandong.common.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.yuandong.argumentresolver.WeChatArgumentResolver;
import com.yuandong.filter.WebContextFilter;

@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {

	@Autowired
	private WeChatArgumentResolver weChatArgumentResolver;
	@Autowired
	private WebContextFilter webContextFilter;
	@Value("${http.multipart.uploadPathPrefix}")
	private String UPLOADED_FOLDER;

	/**
	 * 配置ArgumentResolvers 实现采用业务逻辑，向controllor 方法中注入参数
	 * 
	 * @param argumentResolvers
	 */
	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
		argumentResolvers.add(weChatArgumentResolver);
	}
	
	@Bean
    public FilterRegistrationBean testFilterRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(webContextFilter);//添加过滤器
        registration.addUrlPatterns("/*");//设置过滤路径，/*所有路径
        registration.setName("webContextFilter");//设置优先级
        registration.setOrder(1);//设置优先级
        return registration;
    }
	
	@Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //上传的图片在D盘下的OTA目录下，访问路径如：http://localhost:8081/OTA/d3cf0281-bb7f-40e0-ab77-406db95ccf2c.jpg
        //其中OTA表示访问的前缀。"file:D:/OTA/"是文件真实的存储路径
        registry.addResourceHandler("/distribution-filesystem/**").addResourceLocations("file:"+UPLOADED_FOLDER);
    }
}
