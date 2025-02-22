package com.project.marathon.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;
import java.util.UUID;

@Configuration
@MapperScan("com.project.marathon.mapper")  // Mapper 인터페이스 자동 감지
public class MyBatisConfig {

    @Bean
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(dataSource);

        //여러 개의 매퍼 파일을 자동으로 등록 mapper 관리
        factoryBean.setMapperLocations(new PathMatchingResourcePatternResolver()
                .getResources("classpath*:/mappers/*.xml"));

        factoryBean.setTypeAliasesPackage("com.project.marathon.entity, com.project.marathon.dto"); // DTO 자동 매핑

        // TypeHandler 직접 등록
        SqlSessionFactory sqlSessionFactory = factoryBean.getObject();
        TypeHandlerRegistry typeHandlerRegistry = sqlSessionFactory.getConfiguration().getTypeHandlerRegistry();
        typeHandlerRegistry.register(UUID.class, new UUIDTypeHandler());


        return factoryBean.getObject();
    }

}

