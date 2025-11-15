package com.mulberry.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mulberry.common.CommonConst;
import com.mulberry.common.R;
import com.mulberry.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final ObjectMapper objectMapper;
    private final StringRedisTemplate redisTemplate;

    public JwtAuthenticationFilter(
            JwtUtil jwtUtil,
            ObjectMapper objectMapper,
            StringRedisTemplate redisTemplate
    ) {
        this.jwtUtil = jwtUtil;
        this.objectMapper = objectMapper;
        this.redisTemplate = redisTemplate;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        final String header = request.getHeader("Authorization");

        if (header == null || !header.startsWith(CommonConst.OAuthToken)) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = header.substring(CommonConst.OAuthToken.length());
        try {
            String username = jwtUtil.extractUsername(token);
            if (!jwtUtil.validateToken(token) || username == null) {
                sendMessage(response, "Invalid or expired token");
                return;
            }
            String tokenOwner = redisTemplate.opsForValue().get(token);
            if (!username.equals(tokenOwner)) {
                sendMessage(response, "Token expired");
                return;
            }

            UserDetails userDetails = org.springframework.security.core.userdetails.User
                    .withUsername(username)
                    .password("")
                    .authorities("ROLE_USER")
                    .build();
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);

            filterChain.doFilter(request, response);
        } catch (Exception e) {
            logger.warn("Invalid JWT token: " + e.getMessage());
            sendMessage(response, "Malformed or invalid token");
        }
    }

    private void sendMessage(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(R.error(message)));
    }
}
