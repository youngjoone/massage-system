
package com.example.massagesystem.auth;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private final JwtTokenProvider tokenProvider;
    private final UserDetailsService userDetailsService;

    // 필터가 적용되지 않을 경로 목록
    private static final List<String> EXCLUDE_URLS = Arrays.asList(
            "/api/auth/login",
            "/api/auth/register"
    );

    public JwtAuthenticationFilter(JwtTokenProvider tokenProvider, UserDetailsService userDetailsService) {
        this.tokenProvider = tokenProvider;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        logger.debug("Checking if filter should be skipped for path: {}", path);
        boolean skip = EXCLUDE_URLS.stream().anyMatch(path::startsWith);
        if (skip) {
            logger.debug("Skipping JwtAuthenticationFilter for path: {}", path);
        }
        return skip;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        logger.debug("Entering JwtAuthenticationFilter for request URI: {}", request.getRequestURI());
        try {
            String jwt = getJwtFromRequest(request);
            logger.debug("Extracted JWT from request: {}", jwt != null ? "Present" : "Not Present");

            if (jwt != null && tokenProvider.validateToken(jwt)) {
                String username = tokenProvider.getUsernameFromJWT(jwt);
                logger.debug("JWT is valid. Username: {}", username);

                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
                logger.debug("User {} authenticated successfully.", username);
            } else if (jwt != null) {
                logger.warn("JWT is present but invalid or expired.");
            }
        } catch (Exception ex) {
            logger.error("Could not set user authentication in security context: {}", ex.getMessage(), ex);
        }

        filterChain.doFilter(request, response);
        logger.debug("Exiting JwtAuthenticationFilter for request URI: {}", request.getRequestURI());
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                logger.debug("Found cookie: {}={}", cookie.getName(), cookie.getValue());
                if ("accessToken".equals(cookie.getName())) {
                    logger.debug("Found accessToken cookie.");
                    return cookie.getValue();
                }
            }
        }
        logger.debug("accessToken cookie not found.");
        return null;
    }
}
