package com.demo.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.demo.service.CustomUserDetailService;
import com.demo.util.JwtUtilToken;

@Component
public class JwtFilter extends OncePerRequestFilter{
	
	@Autowired
	private JwtUtilToken jwtUtilToken;
	
	@Autowired
	private CustomUserDetailService service;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
		final String requestTokenHeader = request.getHeader("Authorization");
		
		String username=null;
		String jwttoken=null;
		
		 if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
			 jwttoken = requestTokenHeader.substring(7);
	            username = jwtUtilToken.getUserNameFromToken(jwttoken);
	        }
		 
		 if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			 
			 UserDetails userDetails = service.loadUserByUsername(username);
//			 if (jwtUtilToken.validateToken(jwttoken, userDetails)) {
	                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
	                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
	                usernamePasswordAuthenticationToken
	                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
	                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
	           
//	        }
		 }
	        chain.doFilter(request, response);
		
	}
	

}
