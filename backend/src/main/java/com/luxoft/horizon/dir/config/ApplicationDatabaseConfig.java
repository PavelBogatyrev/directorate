package com.luxoft.horizon.dir.config;


import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.jdbc.JndiDataSourceAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;
import org.springframework.jmx.export.MBeanExporter;
import org.springframework.jmx.support.JmxUtils;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;

/**
 * @author rlapin
 */
@Configuration

@EnableJpaRepositories(
        basePackages = "com.luxoft.horizon.dir.service.app",
        entityManagerFactoryRef = "appEntityManager",
        transactionManagerRef = "appTransactionManager"
)
public class ApplicationDatabaseConfig{

    @Autowired
    private AppJPAConfig config;
    @Autowired
    private ApplicationContext context;



    @Bean
    @Primary
    @ConfigurationProperties(prefix = "datasource_app")
    public DataSource appDataSource() {
        if(config.getJndiName()!=null && !config.getJndiName().isEmpty()) {
            JndiDataSourceLookup dataSourceLookup = new JndiDataSourceLookup();
            DataSource dataSource = dataSourceLookup.getDataSource(config.getJndiName());
            this.excludeMBeanIfNecessary(dataSource, "appDataSource");
            return dataSource;
        }else{
            return DataSourceBuilder.create().build();
        }
    }




    @Bean
    @Primary
    @ConfigurationProperties(prefix = "datasource_app")
    public JpaVendorAdapter appVendorAdapter(){
        HibernateJpaVendorAdapter hibernateJpaVendorAdapter = new HibernateJpaVendorAdapter();
        hibernateJpaVendorAdapter.setShowSql(config.isShowSql());
        return hibernateJpaVendorAdapter;
    }

    @Bean
    @Primary
    public LocalContainerEntityManagerFactoryBean appEntityManager() {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(appDataSource());
        em.setPackagesToScan("com.luxoft.horizon.dir.entities.app");

        em.setJpaVendorAdapter(appVendorAdapter());
        HashMap<String, Object> properties = new HashMap<String, Object>();
        properties.put("hibernate.hbm2ddl.auto", config.getDdlAuto());
        properties.put("hibernate.dialect", config.getDbplatform());
        em.setJpaPropertyMap(properties);

        return em;
    }
    @Primary
    @Bean
    public PlatformTransactionManager appTransactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(appEntityManager().getObject());
        return transactionManager;
    }
    private void excludeMBeanIfNecessary(Object candidate, String beanName) {
        try {
            MBeanExporter mbeanExporter = (MBeanExporter)this.context.getBean(MBeanExporter.class);
            if(JmxUtils.isMBean(candidate.getClass())) {
                mbeanExporter.addExcludedBean(beanName);
            }
        } catch (NoSuchBeanDefinitionException ignored) {
        }

    }

}
