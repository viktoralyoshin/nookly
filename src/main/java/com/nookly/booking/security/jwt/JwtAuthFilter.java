package com.nookly.booking.security.jwt;

import com.nookly.booking.exception.jwt.JwtAuthenticationException;
import com.nookly.booking.security.service.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {
    private final JwtProvider jwtProvider;
    private final CustomUserDetailsService userDetailsService;

    public JwtAuthFilter(final JwtProvider jwtProvider, @Lazy CustomUserDetailsService userDetailsService) {
        this.jwtProvider = jwtProvider;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain
    ) throws ServletException, IOException {
        try {
            String token = jwtProvider.extractTokenFromRequest(request);
            if (token != null && jwtProvider.validateToken(token)) {
                setAuthentication(token);
            }
        } catch (JwtAuthenticationException | UsernameNotFoundException e) {
            sendErrorResponse(response, HttpStatus.FORBIDDEN, e.getMessage());
            return;
        }

        filterChain.doFilter(request, response);
    }

    private void sendErrorResponse(HttpServletResponse response,
                                   HttpStatus status,
                                   String message
    ) throws IOException {
        response.setStatus(status.value());
        response.getWriter().write("{\"error\": \"" + message + "\"}");
        response.setContentType("application/json");
    }

    private void setAuthentication(String token) {
        String username = jwtProvider.getUsernameFromToken(token);

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
        );

        SecurityContextHolder.getContext().setAuthentication(auth);
    }
}
