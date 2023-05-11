package com.rick.cursomc.configurations;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rick.cursomc.domain.PagamentoComBoleto;
import com.rick.cursomc.domain.PagamentoComCartao;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

public class JacksonConfig {
    @Bean
    public Jackson2ObjectMapperBuilder objectMapperBuilder() {
        Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder() {
            public void configure(ObjectMapper objectMapper) {
                objectMapper.registerSubtypes(PagamentoComCartao.class);
                objectMapper.registerSubtypes(PagamentoComBoleto.class);
                super.configure(objectMapper);

            }
        };
        return builder;
    }
}
