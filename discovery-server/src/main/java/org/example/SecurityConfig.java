package org.example;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Value("${app.eureka.username}")
    private String username;
    @Value("${app.eureka.password}")
    private String password;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // Cấu hình bảo mật HTTP với lambda
        http.csrf(csrf -> csrf.disable())  // Tắt CSRF
                .authorizeHttpRequests(authorize -> authorize
                        .anyRequest().authenticated()  // Yêu cầu xác thực cho tất cả các yêu cầu
                )
                .httpBasic(withDefaults());  // Cung cấp HTTP Basic Authentication
        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        // Cấu hình UserDetailsService với người dùng in-memory
        return new InMemoryUserDetailsManager(
                User.withUsername("eureka")
                        .password("password")  // Không mã hóa mật khẩu
                        .roles("USER")
                        .build()
        );
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();  // Không mã hóa mật khẩu
    }
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);

        authenticationManagerBuilder
                .userDetailsService(userDetailsService())
                .passwordEncoder(NoOpPasswordEncoder.getInstance());  // Không mã hóa mật khẩu

        return authenticationManagerBuilder.build();
    }
}
