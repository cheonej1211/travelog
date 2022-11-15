package com.travelog.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.travelog.config.oAuth.OAuthService;
import com.travelog.config.oAuth.OAuthSuccessHandler;
import com.travelog.config.security.JwtAuthenticationFilter;
import com.travelog.config.security.JwtTokenProvider;

import lombok.RequiredArgsConstructor;

@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

	private final JwtTokenProvider jwtTokenProvider;
	private final OAuthService oAuthService;
	private final OAuthSuccessHandler oAuthSuccessHandler;

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	/*
	 * @Bean public HttpFirewall DefaultHttpFirewall() { return new
	 * DefaultHttpFirewall(); }
	 */
	@Bean
	public WebSecurityCustomizer webSecurityCustomizer() {
		return (web) -> web.ignoring().mvcMatchers("/resources/**", "/static/**", "/css/**", "/js/**", "/images/**");
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.csrf().disable();

		http.headers().frameOptions().disable();
		http.httpBasic().disable().csrf().disable().sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().authorizeRequests()
				.antMatchers("/login", "/member/join").permitAll()
				// .and() .authorizeRequests().antMatchers("/**").permitAll()
				.antMatchers("/admin/**").hasRole("ADMIN").anyRequest().authenticated().and()
				.addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider),
						UsernamePasswordAuthenticationFilter.class)
				.authorizeRequests()

				// .and().formLogin().loginPage("/login").loginProcessingUrl("/sec").failureUrl("/login?error=true")

				.and().logout().logoutUrl("/logout").logoutSuccessUrl("/login")
				.deleteCookies(JwtTokenProvider.TOKEN_NAME)

				.and() // 추가
				.oauth2Login() // OAuth2기반의 로그인인 경우
				.loginPage("/login").defaultSuccessUrl("/login").successHandler(oAuthSuccessHandler).failureUrl("/login")
				.userInfoEndpoint() // 로그인 성공 후 사용자정보를 가져온다
				.userService(oAuthService) // 사용자정보를 처리할 때 사용한다

		;
		return http.build();
	}

}
