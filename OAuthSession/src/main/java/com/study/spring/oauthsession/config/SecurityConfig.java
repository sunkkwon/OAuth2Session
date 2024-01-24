package com.study.spring.oauthsession.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.client.OAuth2LoginConfigurer.UserInfoEndpointConfig;
import org.springframework.security.web.SecurityFilterChain;

import com.study.spring.oauthsession.service.CustomOAuth2UserService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	private final CustomOAuth2UserService customOAuth2UserService;
	
	public SecurityConfig(CustomOAuth2UserService customOAuth2UserService) {
		super();
		this.customOAuth2UserService = customOAuth2UserService;
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		// csrf custom setting
		http.csrf((csrf) -> csrf.disable());

		// From 로그인 방식 disable
		http.formLogin((login) -> login.disable());

		// http basic 인증 방식 disable
		http.httpBasic((basic) -> basic.disable());

		// basic disable
		http.oauth2Login((oauth2) -> oauth2.userInfoEndpoint((UserInfoEndpointConfig) -> UserInfoEndpointConfig.userService(customOAuth2UserService)));

		// 경로별 인가 작업
		http.authorizeHttpRequests((auth) -> auth.requestMatchers("/").permitAll() // 3개의 경우는 허용
				                                 .anyRequest().authenticated()); // 이외의 경우는 로그인 인증필요

		// 세션 설정
		// JWT를 통한 인증/인가를 위해서 세션을 STATELESS 상태로 설정하는 것이 중요하다.
//        http.sessionManagement((session) -> session
//            .sessionCreationPolicy(SessionCreationPolicy.STATELESS));
//
		return http.build();
	}
}
