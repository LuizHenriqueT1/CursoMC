package com.rick.cursomc.configurations;

import com.rick.cursomc.security.constant.ApiPathExclusion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig  {

    @Autowired
    private Environment env;

    private static final String[] PUBLIC_MATCHERS ={"/h2-console/**", "/produtos/**", "/categorias/**"};

    @Bean
    public SecurityFilterChain configure (HttpSecurity http) throws Exception {

        if(Arrays.asList(env.getActiveProfiles()).contains("test")) {
            http.headers().frameOptions().disable();
        }

        http.cors().and().csrf().disable().exceptionHandling().and().sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
        .authorizeHttpRequests(auth -> auth.requestMatchers(HttpMethod.GET,
                        List.of(ApiPathExclusion.GetApiPathExclusion.values()).stream()
                                .map(ApiPathExclusion.GetApiPathExclusion::getPath).toArray(String[]::new)).permitAll()
                .anyRequest().authenticated());
        return http.build();
    }
}
