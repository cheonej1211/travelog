package com.travelog.config.security;

//import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import java.security.Key;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.travelog.domain.MemberRole;
import com.travelog.login.controller.LoginController;
import com.travelog.member.service.MemberService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Component
@Slf4j
public class JwtTokenProvider {

	public static final String TOKEN_NAME = "token";

	// private String secretKey = "약오르지까꿍꿍까지르오약약오르지까꿍꿍까지르오약약오르지까꿍꿍까지르오약";
	@Value("${jwt.secret}")
	private String secretKey;

	// 토큰 유효시간 30분
	private long tokenValidTime = 30 * 60 * 1000L;
	private final MemberService memberService;

//	private final UserDetailsService userDetailsService;
//	
//	public JwtTokenProvider(@Lazy UserDetailsService userDetailsService) {
//		this.userDetailsService = userDetailsService;
//	}

	// 객체 초기화, secretKey를 Base64로 인코딩한다.
	@PostConstruct
	protected void init() {
		secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
	}

	// JWT 토큰 생성
	public String createToken(String loginId, MemberRole role) {
		Claims claims = Jwts.claims().setSubject(loginId); // JWT payload 에 저장되는 정보단위
		claims.put("roles", role); // 정보는 key / value 쌍으로 저장된다.
		Date now = new Date();
		byte[] keyBytes = Decoders.BASE64.decode(secretKey);
		Key key = Keys.hmacShaKeyFor(keyBytes);

		return Jwts.builder().setClaims(claims) // 정보 저장
				.setIssuedAt(now) // 토큰 발행 시간 정보
				.setExpiration(new Date(now.getTime() + tokenValidTime)) // set Expire Time
				.signWith(key, SignatureAlgorithm.HS256) // 사용할 암호화 알고리즘과
				// signature 에 들어갈 secret값 세팅
				.compact();
	}

	// JWT 토큰에서 인증 정보 조회
	public Authentication getAuthentication(String token) {
		UserDetails userDetails = memberService.loadUserByUsername(this.getUserPk(token));
		return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
	}

	// 토큰에서 회원 정보 추출
	public String getUserPk(String token) {
		return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody().getSubject();
	}

	// cookie에서 토큰 정보 가져오기
	public String resolveToken(HttpServletRequest request) {
		String token = "";
		if (request.getCookies() != null) {
			token = Arrays.stream(request.getCookies())
					.filter(cookie -> cookie.getName().equals(JwtTokenProvider.TOKEN_NAME)).findFirst()
					.map(Cookie::getValue).orElse("");
		}
		return token;
	}

	// 토큰의 유효성 확인
	public boolean validateToken(String jwtToken) {

		/*
		 * try { Jws<Claims> claims =
		 * Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(jwtToken
		 * ); return !claims.getBody().getExpiration().before(new Date()); } catch
		 * (Exception e) { return false; }
		 */
		
		try {
			Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(jwtToken);
			return true;
		} catch (SignatureException ex) {
			log.error("Invalid JWT signature");
		} catch (MalformedJwtException ex) {
			log.error("Invalid JWT token");
		} catch (ExpiredJwtException ex) {
			log.error("Expired JWT token");
		} catch (UnsupportedJwtException ex) {
			log.error("Unsupported JWT token");
		} catch (IllegalArgumentException ex) {
			log.error("JWT claims string is empty.");
		}
		
		return false;
	}

}
