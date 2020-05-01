package com.github.charlesvhe.support;

import com.querydsl.sql.Configuration;
import com.querydsl.sql.MySQLTemplates;
import com.querydsl.sql.SQLQueryFactory;
import com.querydsl.sql.SpringSqlListener;
import org.h2.tools.Server;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.web.client.RestTemplate;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.sql.DataSource;

@EnableSwagger2
@SpringBootApplication
@MapperScan(basePackages = "com.github.charlesvhe.support.mapper")
public class SupportApplication {
    public SupportApplication() {
        // 开启 H2database TCP服务端
        try {
            Server.createTcpServer("-tcpAllowOthers", "-ifNotExists").start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public SQLQueryFactory queryFactory(DataSource dataSource) {
        Configuration configuration = new Configuration(MySQLTemplates.builder().build());
        configuration.addListener(new SpringSqlListener(dataSource));
        return new SQLQueryFactory(
                configuration,
                () -> DataSourceUtils.getConnection(dataSource));
    }

    @Bean
    public Docket docket() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select().apis(RequestHandlerSelectors.basePackage(this.getClass().getPackage().getName())).build()
                .enable(true);
    }

    public static void main(String[] args) {
        SpringApplication.run(SupportApplication.class, args);
    }

}
