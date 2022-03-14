package com.example.demo.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.StringUtils;

/* 예제용-실제사용x */
public class ExampleServletFilter extends HttpFilter {
	
	private TokenProvider tokenProvider;
	
	
	protected void doFileter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) 
		throws IOException, ServletException {
		try {
			final String token = parseBearerToken(request);
			
			if(token != null && !token.equalsIgnoreCase("null")) {
				//userId 가져오기. 위조된 경우 예외처리
				String userId = tokenProvider.validateAndGetUserId(token);
				
				//다음 ServletFilter 실행
				filterChain.doFilter(request, response);
			}
			
		} catch (Exception e) {
			//예외 발생 시 response를 403 Forbidden으로 설정
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
			
		}
	}
	
	private String parseBearerToken(HttpServletRequest request) {
		//Http 요청의 헤더를 파싱해 Bearer토큰을 리턴한다
		String bearerToken = request.getHeader("Authorizotion");
		
		if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
			return bearerToken.substring(7);
		}
		return null;
		
	}
	
}
