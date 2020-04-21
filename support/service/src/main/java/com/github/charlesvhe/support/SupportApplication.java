package com.github.charlesvhe.support;

import com.querydsl.sql.MySQLTemplates;
import com.querydsl.sql.SQLQueryFactory;
import com.querydsl.sql.spring.SpringConnectionProvider;
import org.h2.tools.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.sql.DataSource;

@EnableSwagger2
@SpringBootApplication
public class SupportApplication {
    @Bean
    public SQLQueryFactory queryFactory(DataSource dataSource) {
        return new SQLQueryFactory(
                new com.querydsl.sql.Configuration(MySQLTemplates.builder().build()),
                new SpringConnectionProvider(dataSource));
    }

    @Bean
    public Docket docket() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select().apis(RequestHandlerSelectors.basePackage(this.getClass().getPackage().getName())).build()
                .enable(true);
    }

    public static void main(String[] args) {
        // 开启 H2database TCP服务端
        try {
            Server.createTcpServer("-tcpAllowOthers", "-ifNotExists").start();
        } catch (Exception e) {
            e.printStackTrace();
        }
        SpringApplication.run(SupportApplication.class, args);
    }

}
