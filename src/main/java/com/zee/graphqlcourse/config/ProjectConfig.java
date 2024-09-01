package com.zee.graphqlcourse.config;

import net.datafaker.Faker;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author : Ezekiel Eromosei
 * @code @created : 31 Aug, 2024
 */

@Configuration
public class ProjectConfig {

    @Bean
    public Faker faker() {
        return new Faker();
    }

}
