package com.yuandong.common.config;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "spring")
public class ApplicationConfigModel {
	//数据库访问配置
	private Datasource datasource1;
	
	private Jpa jpa;
	
	@Data
	public static class Jpa{
		private String database;
	    private boolean showSql;
	    private boolean generateDdl;
	    // Hibernate ddl auto (create, create-drop, update)
	    private String hibernateDdlAuto;  
	    private String databasePlatform;  
	    private String hibernateNamingStrategy;//=org.hibernate.cfg.ImprovedNamingStrategy  
	    private String hibernateDialect;//=org.hibernate.dialect.MySQL5Dialect
	}
	
	@Data
	public static class Datasource{
		private String type;
		private String url;
		private String username;
		private String password;
		private String driverClassName;
		private int initialSize;
		private int minIdle;
		private int maxActive ;
		private int maxWait;
		private int timeBetweenEvictionRunsMillis;
		private int minEvictableIdleTimeMillis;
		private String validationQuery;
		private boolean testWhileIdle;
		private boolean testOnBorrow;
		private boolean testOnReturn;
		private boolean poolPreparedStatements;
		private int maxPoolPreparedStatementPerConnectionSize;
		private String filters;
		private String connectionProperties;
		private boolean useGlobalDataSourceStat;
	}
	
}
