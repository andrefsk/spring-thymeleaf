/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.thymeleafspringapp.config;

import java.util.Properties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

/**
 *
 * @author andrey
 */

public class SecurityConfig{

    public SecurityConfig(){}
    
    @Configuration
    @EnableWebMvcSecurity
    @Profile({"test", "production"})
    public static class SecurityConfigProd extends WebSecurityConfigurerAdapter {

        public SecurityConfigProd() {
            super();
        }

        public SecurityConfigProd(boolean disableDefaults) {
            super(disableDefaults);
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
                    .authorizeRequests()
                    .antMatchers("/resources/**", "/signup", "/home", "/", "/signin/**").permitAll()
                    .anyRequest().authenticated()
                    .and()
                    .formLogin()
                    .loginPage("/login")
                    .permitAll()
                    .and()
                    .logout()
                    .logoutUrl("/logout")
                    .permitAll();
        }

        @Autowired
        public void configureGlobal(UserDetailsService userDetailsService, AuthenticationManagerBuilder auth, PasswordEncoder encoder) throws Exception {
            auth
                    .userDetailsService(userDetailsService)
                    .passwordEncoder(encoder);
            System.out.println();
//        auth.inMemoryAuthentication().withUser("user").password("pendos90").roles("USER");
//        auth.inMemoryAuthentication().withUser("admin").password("admin").roles("ADMIN");
//        auth.inMemoryAuthentication().withUser("dba").password("123456").roles("DBA");

        }

        @Bean
        public PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }
    }

    @Configuration
    @EnableWebMvcSecurity
    @Profile({"dev", "default"})
    public static class SecurityConfigDev extends WebSecurityConfigurerAdapter {

        public SecurityConfigDev() {
            super();
        }

        public SecurityConfigDev(boolean disableDefaults) {
            super(disableDefaults);
        }
        

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
                    .authorizeRequests()
                    .antMatchers("/resources/**", "/signup", "/home", "/", "/signin/**").permitAll()
                    .anyRequest().authenticated()
                    .and()
                    .formLogin()
                    .loginPage("/login")
                    .permitAll()
                    .and()
                    .logout()
                    .logoutUrl("/logout")
                    .permitAll()
                    .and()
                    .rememberMe();
            //   .and()
            //   .apply(new SpringSocialConfigurer());
        }

        @Autowired
        public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {

            auth.userDetailsService(inMemoryUserDetailsManager());
//        auth.inMemoryAuthentication().withUser("user").password("pendos90").roles("USER");
//        auth.inMemoryAuthentication().withUser("admin").password("admin").roles("ADMIN");
//        auth.inMemoryAuthentication().withUser("dba").password("123456").roles("DBA");

        }

        @Bean
        public InMemoryUserDetailsManager inMemoryUserDetailsManager() {
            final Properties users = new Properties();
            users.put("user", "pass,ROLE_USER,enabled"); //add whatever other user you need
            return new InMemoryUserDetailsManager(users);
        }
    }

}
