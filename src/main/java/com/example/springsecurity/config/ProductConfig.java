package com.example.springsecurity.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        prePostEnabled = true
)
public class ProductConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .inMemoryAuthentication()
                .withUser("director1").password(passwordEncoder().encode("director1")).roles("DIRECTOR").authorities("READ_ALL_PRODUCT", "EDIT_PRODUCT", "ADD_PRODUCT", "READ_ONE_PRODUCT", "DELETE_PRODUCT")
                .and()
                //.withUser("manager").password(passwordEncoder().encode("manager")).roles("MANAGER")
                .withUser("director2").password(passwordEncoder().encode("director2")).roles("DIRECTOR").authorities("READ_ALL_PRODUCT", "EDIT_PRODUCT", "ADD_PRODUCT", "READ_ONE_PRODUCT")
                .and()
                .withUser("user").password(passwordEncoder().encode("user")).roles("USER");
    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()    //Authorize request
                // /api/product/* means only GET by id and /api/product/** means only GET which returns list of products
//                .antMatchers(HttpMethod.GET, "/api/product/*").hasAnyRole("USER","DIRECTOR","MANAGER")  //all roles are permitted to GET by ID
//                .antMatchers(HttpMethod.GET,"/api/product/**").hasAnyRole("DIRECTOR","MANAGER")     //only DIRECTOR and MANAGER are permitted (to GET)
//                .antMatchers("/api/product/**").hasRole("DIRECTOR")     //only DIRECTOR is permitted to POST, PUT, DELETE
//                .antMatchers(HttpMethod.DELETE, "/api/product/*").hasAuthority("DELETE_PRODUCT")
//                .antMatchers("/api/product/**").hasAnyAuthority("READ_ALL_PRODUCT", "EDIT_PRODUCT", "ADD_PRODUCT", "READ_ONE_PRODUCT")
                .anyRequest()       //Authorize any requests
                .authenticated()    //Check(user,password) and Authorize any request
                .and()              //Check(user,password) and Authorize any request and
                .httpBasic();       //Check(user,password) and Authorize any request and in httpBasic form
    }
}
