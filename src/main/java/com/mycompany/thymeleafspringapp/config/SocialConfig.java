/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.thymeleafspringapp.config;

import com.mycompany.thymeleafspringapp.service.SimpleSigninAdapter;
import com.mycompany.thymeleafspringapp.service.UserService;
import java.util.ArrayList;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.social.UserIdSource;
import org.springframework.social.config.annotation.ConnectionFactoryConfigurer;
import org.springframework.social.config.annotation.EnableSocial;
import org.springframework.social.config.annotation.SocialConfigurer;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.ConnectionSignUp;
import org.springframework.social.connect.UserProfile;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.jdbc.JdbcUsersConnectionRepository;
import org.springframework.social.connect.mem.InMemoryUsersConnectionRepository;
import org.springframework.social.connect.web.ConnectController;
import org.springframework.social.connect.web.ProviderSignInController;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;
import org.springframework.social.security.AuthenticationNameUserIdSource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 *
 * @author andrey
 */
@Configuration
@EnableWebMvc
public abstract class SocialConfig implements SocialConfigurer {

    Logger log = LoggerFactory.getLogger("SocialConfig");

    public SocialConfig() {
    }

    @Override
    public void addConnectionFactories(ConnectionFactoryConfigurer cfc, Environment e) {
        final FacebookConnectionFactory facebookConnectionFactory = getFacebookConnectionFactory();
        cfc.addConnectionFactory(facebookConnectionFactory);
    }

    @Override
    public UserIdSource getUserIdSource() {
        return new AuthenticationNameUserIdSource();
    }
    
    @Bean
    public ConnectController connectController(
            ConnectionFactoryLocator connectionFactoryLocator,
            ConnectionRepository connectionRepository) {
        ConnectController controller = new ConnectController(connectionFactoryLocator, connectionRepository);

        return controller;
    }

    @Bean
    public ProviderSignInController providerSignInController(
            ConnectionFactoryLocator connectionFactoryLocator,
            UsersConnectionRepository usersConnectionRepository) {
        ProviderSignInController controller = new ProviderSignInController(
                connectionFactoryLocator,
                getUsersConnectionRepository(connectionFactoryLocator),
                new SimpleSigninAdapter());
        return controller;
    }

    protected FacebookConnectionFactory getFacebookConnectionFactory() {
        final FacebookConnectionFactory facebookConnectionFactory = new FacebookConnectionFactory("324403211086437", "48d13789f9af24d5eb4ab4e3c742f6cf");
        facebookConnectionFactory.setScope("email");
        return facebookConnectionFactory;
    }

    @Configuration
    @EnableSocial
    @EnableWebMvc
    @Profile({"dev", "default"})
    public static class SocialConfigDev extends SocialConfig {

        @Autowired
        InMemoryUserDetailsManager auth;

        @Override
        public UsersConnectionRepository getUsersConnectionRepository(ConnectionFactoryLocator connectionFactoryLocator) {
            InMemoryUsersConnectionRepository userRep = new InMemoryUsersConnectionRepository(connectionFactoryLocator);
            userRep.setConnectionSignUp(new ConnectionSignUp() {

                @Override
                public String execute(Connection<?> cnctn) {
                    try {
                        UserProfile profile = cnctn.fetchUserProfile();
                        String email = cnctn.fetchUserProfile().getEmail();
                        auth.createUser(new User(email, "", new ArrayList<GrantedAuthority>()));
                        return profile.getName();
                    } catch (Exception ex) {
                        log.error("SIGNUP", ex.toString(), ex);
                        throw new RuntimeException(ex);
                    }

                }
            });
            return userRep;
        }
    }

    @Configuration
    @EnableSocial
    @EnableWebMvc
    @Profile({"test", "production"})
    public static class SocialConfigProd extends SocialConfig {

        @Autowired
        private DataSource dataSource;
        @Autowired
        UserService userService;

//        @Autowired
//        TextEncryptor encryptor;
        @Override
        public UsersConnectionRepository getUsersConnectionRepository(ConnectionFactoryLocator connectionFactoryLocator) {

            JdbcUsersConnectionRepository userRep = new JdbcUsersConnectionRepository(dataSource, connectionFactoryLocator, textEncryptor());
            userRep.setConnectionSignUp(new ConnectionSignUp() {
                @Override
                public String execute(Connection<?> cnctn) {
                    try {
                        UserProfile profile = cnctn.fetchUserProfile();
                        String email = cnctn.fetchUserProfile().getEmail();
                        userService.createNewUser(profile.getName(), null, email);
                        return profile.getName();
                    } catch (Exception ex) {
                        log.error("SIGNUP", ex.toString(), ex);
                        throw new RuntimeException(ex);
                    }
                }
            });
            return userRep;
        }

        @Bean
        public TextEncryptor textEncryptor() {
            return Encryptors.queryableText("AA", "AA");
        }

        @Bean
        public DataSource getDataSource() {
            DriverManagerDataSource ds = new DriverManagerDataSource("jdbc:postgresql://localhost:5432/trdr", "trdr", "trdr");
            return ds;
        }
    }
}
