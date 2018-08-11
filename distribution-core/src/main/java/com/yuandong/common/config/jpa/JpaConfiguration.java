package com.yuandong.common.config.jpa;

import java.util.Map;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.support.PersistenceAnnotationBeanPostProcessor;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.yuandong.common.config.ApplicationConfigModel;
import com.yuandong.common.support.jpa.repository.BaseRepositoryFactoryBean;

@Configuration
@EnableJpaAuditing
@EnableTransactionManagement //启注解事务管理，等同于xml配置方式的 <tx:annotation-driven />
@EnableJpaRepositories(
		repositoryFactoryBeanClass = BaseRepositoryFactoryBean.class,
        entityManagerFactoryRef="primaryEntityManagerFactory",
        transactionManagerRef="primaryTransactionManager",
        basePackages= {"com.**.repository"}) //设置Repository所在位置
@EntityScan(basePackages = "com.**.entity")
public class JpaConfiguration {
	@Autowired
	private JpaProperties jpaProperties;
	@Autowired
	private ApplicationConfigModel applicationConfigModel;
    @Autowired 
    @Qualifier("primaryDataSource")
    private DataSource primaryDataSource;

    @Bean
    @Primary
    public JpaVendorAdapter jpaVendorAdapter() {
            HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
            adapter.setDatabase(Database.valueOf(applicationConfigModel.getJpa().getDatabase()));
            adapter.setShowSql(applicationConfigModel.getJpa().isShowSql());
            adapter.setGenerateDdl(applicationConfigModel.getJpa().isGenerateDdl());
            adapter.setDatabasePlatform(applicationConfigModel.getJpa().getDatabasePlatform());
            return adapter;
    }

    @Bean(name = "primaryEntityManagerFactory") 
    @Primary
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(EntityManagerFactoryBuilder builder) {
        return builder
                .dataSource(primaryDataSource)
                .properties(getVendorProperties(primaryDataSource))
//                .packages(applicationConfigModel.getModel().getPrimaryPackage()) //设置实体类所在位置
                .persistenceUnit("primaryPersistenceUnit")
                .build();
    }

    private Map<String, String> getVendorProperties(DataSource dataSource) {
    	Map<String, String> map = jpaProperties.getHibernateProperties(dataSource);
    	map.put("hibernate.dialect", applicationConfigModel.getJpa().getHibernateDialect());
    	return map;
    }

    @Bean(name = "primaryTransactionManager")
    @Primary
    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        JpaTransactionManager manager = new JpaTransactionManager();
        manager.setEntityManagerFactory(entityManagerFactory);
        return manager;
    }

    @Bean
    public BeanPostProcessor persistenceTranslation() {
        return new PersistenceAnnotationBeanPostProcessor();
    }
    
}
