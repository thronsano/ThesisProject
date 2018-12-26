package com.diamondLounge.utility;

import com.diamondLounge.entity.db.*;
import org.apache.tomcat.dbcp.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Objects;
import java.util.Properties;

import static org.hibernate.cfg.Environment.*;

@Configuration
@PropertySource("classpath:db.properties")
@EnableTransactionManagement
public class HibernateConfig {

    private static final Class[] ANNOTATED_CLASSES = new Class[]{User.class, Authority.class, ResetToken.class, Employee.class, Wage.class};

    @Autowired
    Environment env;

    @Bean
    public DataSource getDataSource() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName(env.getProperty("mysql.driver"));
        dataSource.setUrl(env.getProperty("mysql.jdbcUrl"));
        dataSource.setUsername(env.getProperty("mysql.username"));
        dataSource.setPassword(env.getProperty("mysql.password"));

        return dataSource;
    }

    private Properties getHibernateProperties() {
        Properties props = new Properties();

        props.put(SHOW_SQL, Objects.requireNonNull(env.getProperty("hibernate.show_sql")));
        props.put(HBM2DDL_AUTO, Objects.requireNonNull(env.getProperty("hibernate.hbm2ddl.auto")));
        props.put(DIALECT, Objects.requireNonNull(env.getProperty("hibernate.dialect")));
        props.put(POOL_SIZE, Objects.requireNonNull(env.getProperty("hibernate.pool_size")));
        props.put(CURRENT_SESSION_CONTEXT_CLASS, Objects.requireNonNull(env.getProperty("hibernate.context_class")));
        props.put(STATEMENT_BATCH_SIZE, Objects.requireNonNull(env.getProperty("hibernate.jdbc.batch_size")));

        return props;
    }

    @Bean
    public LocalSessionFactoryBean getSessionFactory() {
        LocalSessionFactoryBean factoryBean = new LocalSessionFactoryBean();

        factoryBean.setDataSource(getDataSource());
        factoryBean.setPackagesToScan("com.diamondLounge.entity");
        factoryBean.setHibernateProperties(getHibernateProperties());
        factoryBean.setAnnotatedClasses(ANNOTATED_CLASSES);

        return factoryBean;
    }


    @Bean
    public HibernateTransactionManager getTransactionManager() {
        HibernateTransactionManager transactionManager = new HibernateTransactionManager();
        transactionManager.setSessionFactory(getSessionFactory().getObject());

        return transactionManager;
    }
}
