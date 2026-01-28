package com.cdac.admin.config.SeecurityConfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
//
//@Configuration
//public class Config {
//    
//	 @Bean
//	  public InMemoryUserDetailsManager userDetailsManager(){
//		 
//		  UserDetails admin = User.builder()
//				  .username("admin")
//				  .password("{noop}admin")
//				  .roles("ADMIN")
//				  .build();
//				  
//		 
//		 return new InMemoryUserDetailsManager(admin);
//	 }
//	 
//	 @Bean
//	 public SecurityFilterChain securityFilter(HttpSecurity http) {
//		 http.authorizeHttpRequests(config -> config
//				 .requestMatchers(HttpMethod.GET , "/**").hasRole("ADMIN")
//		 .requestMatchers(HttpMethod.POST , "/**").hasRole("ADMIN")
//		 .requestMatchers(HttpMethod.DELETE , "/**").hasRole("ADMIN")
//		 .requestMatchers(HttpMethod.PUT , "/**").hasRole("ADMIN")
//				 );
//		 
//		 http.csrf(csrf -> csrf.disable());
//		 http.httpBasic(Customizer.withDefaults());
//		return http.build();
//	 }
//}
