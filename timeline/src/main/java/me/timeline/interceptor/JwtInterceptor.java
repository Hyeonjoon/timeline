package me.timeline.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import me.timeline.exception.UnauthorizedException;
import me.timeline.service.JwtService;

@Component
public class JwtInterceptor implements HandlerInterceptor{
	
	private static final String HEADER_AUTH = "Authorization";
	
	@Autowired
	private JwtService jwtService;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
		final String token = request.getHeader(HEADER_AUTH);
		try {
			boolean isValid = token != null && jwtService.JwtIsUsable(token);
			if (!isValid) {
				throw new UnauthorizedException();
			}
			return isValid;
		} catch (Exception e) {
			throw new UnauthorizedException();
		}
	}
}
