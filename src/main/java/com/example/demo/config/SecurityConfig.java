package com.example.demo.config;

import com.example.demo.repasitory.UserRepository;
import com.example.demo.security.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final UserRepository userRepository;

    @Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailsServiceImpl(userRepository);
    }


    @Bean
    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/products/add",
                        "/products/{productId}/edit",
                        "/products/{productId}/charge",
                        "/products/deleteCategory/{id}",
                        "/deleteCategory/{id}", "/addCategory",
                        "/products{productId}/addComment",
                        "/sendMessage", "/admin")
                .hasAnyAuthority("ADMIN")
                .antMatchers("/home",
                        "/products/{productId}/charge", "/products",
                        "/products{productId}/addComment","/sendMessage")
                .hasAnyAuthority("USER", "ADMIN")
                .antMatchers(HttpMethod.GET, "/")
                .permitAll()
                .and()
                .csrf()
                .disable()
                .exceptionHandling().accessDeniedPage("/403")
                .and()
                .formLogin()
                .loginPage("/login").permitAll()
                .and()
                .rememberMe()
                .key("uniqueAndSecret")
                .userDetailsService(userDetailsService())
                .and()
                .logout();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(daoAuthenticationProvider());
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(userDetailsService());
        return provider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}