package com.hoangthien.pitchbooking.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.session.HttpSessionEventPublisher;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private CustomAuthenticationFailureHandler authenticationFailureHandler;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/" ,"/register", "/pitch/**").permitAll()
                .and()
                .formLogin()
                .loginPage("/")
                .usernameParameter("userName")
                .passwordParameter("password")
                .defaultSuccessUrl("/pitch/all")
                .failureHandler(authenticationFailureHandler)
                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/?message=logout")
                .invalidateHttpSession(false)
                .and()
                .exceptionHandling()
                .accessDeniedPage("/403");

        http.sessionManagement().sessionFixation().newSession()
                .invalidSessionUrl("/?message=timeout")
                .maximumSessions(1)
                .expiredUrl("/?message=max_session")
                .maxSessionsPreventsLogin(true);

        http.csrf().disable();
    }
}
