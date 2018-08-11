package com.yuandong.common.config.jpa;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;

import com.alibaba.druid.pool.DruidDataSource;
import com.yuandong.common.config.ApplicationConfigModel;

/**
 * DruidDBConfig类被@Configuration标注，用作配置信息； 
 * DataSource对象被@Bean声明，为Spring容器所管理， 
 * @Primary表示这里定义的DataSource将覆盖其他来源的DataSource。
 * @author ZSX
 *jdbc.url=${jdbc.url} 
 *最新的支持方式如下: 
 *jdbc.url=@jdbc.url@  
 */
@Configuration
public class DruidDBConfig {
    private Logger logger = LoggerFactory.getLogger(DruidDBConfig.class);
    @Autowired
    private ApplicationConfigModel applicationConfigModel;

    @Bean(name="primaryDataSource")
    @Primary // 在同样的DataSource中，首先使用被标注的DataSource
    @Qualifier("primaryDataSource")
    public DataSource primaryDataSource() {
        DruidDataSource datasource = new DruidDataSource();
        datasource.setUrl(applicationConfigModel.getDatasource1().getUrl());
        datasource.setUsername(applicationConfigModel.getDatasource1().getUsername());
        datasource.setPassword(applicationConfigModel.getDatasource1().getPassword());
        datasource.setDriverClassName(applicationConfigModel.getDatasource1().getDriverClassName());

        // configuration
        datasource.setInitialSize(applicationConfigModel.getDatasource1().getInitialSize());
        datasource.setMinIdle(applicationConfigModel.getDatasource1().getMinIdle());
        datasource.setMaxActive(applicationConfigModel.getDatasource1().getMaxActive());
        datasource.setMaxWait(applicationConfigModel.getDatasource1().getMaxWait());
        datasource.setTimeBetweenEvictionRunsMillis(applicationConfigModel.getDatasource1().getTimeBetweenEvictionRunsMillis());
        datasource.setMinEvictableIdleTimeMillis(applicationConfigModel.getDatasource1().getMinEvictableIdleTimeMillis());
        datasource.setValidationQuery(applicationConfigModel.getDatasource1().getValidationQuery());
        datasource.setTestWhileIdle(applicationConfigModel.getDatasource1().isTestWhileIdle());
        datasource.setTestOnBorrow(applicationConfigModel.getDatasource1().isTestOnBorrow());
        datasource.setTestOnReturn(applicationConfigModel.getDatasource1().isTestOnReturn());
        datasource.setPoolPreparedStatements(applicationConfigModel.getDatasource1().isPoolPreparedStatements());
        datasource.setMaxPoolPreparedStatementPerConnectionSize(applicationConfigModel.getDatasource1().getMaxPoolPreparedStatementPerConnectionSize());
        try {
            datasource.setFilters(applicationConfigModel.getDatasource1().getFilters());
        } catch (SQLException e) {
        	logger.error(e.getMessage(),e);
        }
        datasource.setConnectionProperties(applicationConfigModel.getDatasource1().getConnectionProperties());
        return datasource;
    }
    
    @Primary
	@Bean(name = "primaryJdbcTemplate")
	public JdbcTemplate primaryJdbcTemplate(@Qualifier("primaryDataSource") DataSource dataSource) {
	    return new JdbcTemplate(dataSource);
	}
	
}
