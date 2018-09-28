package com.tmsProject.config;

import com.tmsProject.Entity.User;
import com.tmsProject.Repository.UserDetailsRepo;
import com.tmsProject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.boot.autoconfigure.security.oauth2.resource.PrincipalExtractor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;

import java.time.LocalDateTime;

@Configuration
@EnableWebSecurity
@EnableOAuth2Sso
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    UserService userService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .mvcMatchers("/","/enter")
                .permitAll()
                .anyRequest().authenticated()
                .and().logout().logoutSuccessUrl("/").permitAll()
                .and()
                .csrf().disable();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService)
                .passwordEncoder(NoOpPasswordEncoder.getInstance());
    }

    @Bean
    public PrincipalExtractor principalExtractor(UserDetailsRepo userDetailsRepo) {
        return map -> {
              String id=(String) map.get("sub");
              User user = userDetailsRepo.findById(id).orElseGet(()->{
                  User newUser=new User();
                  newUser.setId(id);
                  newUser.setPassword("12345678");//TODO генерить пароли и высылать на email
                  newUser.setName((String) map.get("name"));
                  newUser.setUserpic((String)map.get("picture"));
                  newUser.setEmail((String) map.get("email"));
                  newUser.setGender((String)map.get("gender"));
                  newUser.setLocale((String)map.get("locale"));


                  return newUser;
                  });
              user.setLastVisit(LocalDateTime.now());
              return userDetailsRepo.save(user);
        };
    }

}
