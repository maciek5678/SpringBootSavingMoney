package com.example.demo.security;

import jakarta.servlet.http.Cookie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;


@Component
public class JwtAuthFilter extends OncePerRequestFilter {


    @Autowired
    private JwtUtils jwtUtils;


    @Autowired
    private com.example.demo.service.UserDetailsServiceImpl userDetailsService;





    private String parseJwt(HttpServletRequest request) {
        String token = null;

        if (request.getCookies() != null) {
            for (Cookie c : request.getCookies()) {
                if (c.getName().equals("jwt")) {
                    token = c.getValue();

                    return token;
                }
            }
        }
        if (token != null) {
            logger.error("User expired");
        }


        return null;
    }

    private void refreshToken(String oldToken, HttpServletResponse response) {
        // Tu sprawdzamy, czy token jest blisko wygaśnięcia
        long expiresInMs = jwtUtils.getRemainingDuration(oldToken);

        long threshold = 60 * 60 * 1000; // odnowić jeśli < 55 minut do końca

        if (expiresInMs < threshold) {

            String username = jwtUtils.getUserNameFromJwtToken(oldToken);
            String newToken = jwtUtils.generateTokenFromUsername(username);  // NOWY TOKEN z nową datą!

            Cookie cookie = new Cookie("jwt", newToken);
            cookie.setHttpOnly(true);
            cookie.setSecure(true);
            cookie.setPath("/");
            cookie.setMaxAge(3600); // ważność cookie (nie JWT!)
            response.addCookie(cookie);
        }
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String jwt = parseJwt(request);
            if (jwt != null && jwtUtils.validateJwtToken(jwt)) {

                String username = jwtUtils.getUserNameFromJwtToken(jwt);
                refreshToken(jwt, response);


                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));


                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            logger.error("Cannot set user authentication: {}", e);
        }


        filterChain.doFilter(request, response);
    }
}