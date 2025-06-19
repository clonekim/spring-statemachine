package com.innogrid.config;

import lombok.extern.slf4j.Slf4j;
import org.jooq.conf.RenderQuotedNames;
import org.springframework.boot.autoconfigure.jooq.DefaultConfigurationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class JOOQConfig {


    @Bean
    public DefaultConfigurationCustomizer configurationCustomizer() {
        return configuration -> {
            configuration.settings()
                    .withRenderCatalog(false)
                    .withRenderSchema(false)
                    .withRenderQuotedNames(RenderQuotedNames.NEVER);
        };
    }

}
