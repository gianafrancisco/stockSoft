package yporque.config;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import java.util.Properties;

/**
 * Created by francisco on 04/12/2015.
 */
//@Configuration
//@EnableJpaRepositories("yporque.repository")
class ApplicationConfigurationMysql extends ApplicationConfiguration {

    @Value("${spring.datasource.driverClassName:'com.mysql.jdbc.Driver'}")
    private String driverClassName;
    @Value("${spring.datasource.url:'jdbc:mysql://localhost:3306/yporque'}")
    private String url;
    @Value("${spring.datasource.username:root}")
    private String username;
    @Value("${spring.datasource.password:123456}")
    private String password;

    @Bean
    @Override
    public DataSource dataSource(){
        DataSource dataSource = new DataSource();
        dataSource.setDriverClassName(driverClassName);
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        return dataSource;
    }

    @Override
    public LocalContainerEntityManagerFactoryBean entityManagerFactoryBean(){
        LocalContainerEntityManagerFactoryBean entityManagerFactoryBean= new LocalContainerEntityManagerFactoryBean();

        entityManagerFactoryBean.setDataSource(this.dataSource());
        entityManagerFactoryBean.setPackagesToScan("yporque");
        entityManagerFactoryBean.setPersistenceUnitName("emsPU");

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        entityManagerFactoryBean.setJpaVendorAdapter(vendorAdapter);

        Properties ps = new Properties();
        ps.put("hibernate.dialect", "org.hibernate.dialect.MySQL5Dialect");
        ps.put("hibernate.hbm2ddl.auto", "none");
        ps.put("hibernate.archive.autodetection","class");
        ps.put("hibernate.show_sql","true");

        entityManagerFactoryBean.setJpaProperties(ps);
        entityManagerFactoryBean.afterPropertiesSet();
        return entityManagerFactoryBean;
    }
}

