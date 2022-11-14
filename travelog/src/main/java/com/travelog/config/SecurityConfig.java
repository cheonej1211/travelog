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
import com.travelog.config.security.JwtAuthenticationFilter;
import com.travelog.config.security.JwtTokenProvider;

import lombok.RequiredArgsConstructor;

@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

	private final JwtTokenProvider jwtTokenProvider;
	private final OAuthService oAuthService;

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
		return (web) -> web.ignoring().mvcMatchers("/resources/**", "/static/**", "/css/**", "/js/**");
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

				.and().formLogin().loginPage("/login").loginProcessingUrl("/sec")
				.failureUrl("/login?error=true")
				
				.and()
                .oauth2Login() //OAuth2 로그인 설정 시작점
                .defaultSuccessUrl("/oauth/loginInfo", true) //OAuth2 성공시 redirect
                .userInfoEndpoint() //OAuth2 로그인 성공 이후 사용자 정보를 가져올 때 설정 담당
                .userService(oAuthService) //OAuth2 로그인 성공 시, 작업을 진행할 

		/*
		 * .and().logout().logoutUrl("/logout").logoutSuccessUrl("/login")
		 * .deleteCookies(JwtTokenProvider.TOKEN_NAME)
		 */;
		return http.build();
	}

}
