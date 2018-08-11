package com.yuandong.common.config;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "weixin")
public class WeChatConfig {
	
	private String appid;
	
	private String appsecret;
	
	private String key;
	
	private String mchId;

}
