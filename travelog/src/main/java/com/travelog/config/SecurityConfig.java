package com.travelog.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.firewall.DefaultHttpFirewall;
import org.springframework.security.web.firewall.HttpFirewall;

import com.travelog.config.security.JwtAuthenticationFilter;
import com.travelog.config.security.JwtTokenProvider;

import lombok.RequiredArgsConstructor;

@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

	private final JwtTokenProvider jwtTokenProvider;

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	/*
	 * @Bean public HttpFirewall DefaultHttpFirewall() { return new
	 * DefaultHttpFirewall(); }
	 */
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.csrf().disable();
		http.headers().frameOptions().disable();
		http.httpBasic().disable()
				.csrf().disable()
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				.and()
				.authorizeRequests()
				.antMatchers("/login", "/member/join", "/travelog/**").permitAll()
				/*
				 * .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).
				 * permitAll()
				 * .antMatchers("/resources/**","/static/**","/css/**","/js/**").permitAll()
				 */
				/*
				 * .mvcMatchers( "/resources/**","/static/**","/css/**","/js/**" ).permitAll()
				 */
				.antMatchers("/admin/**").hasRole("ADMIN").anyRequest().authenticated()
				.and()
				.addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider),
						UsernamePasswordAuthenticationFilter.class)
				.authorizeRequests();
		return http.build();
	}
	


}
