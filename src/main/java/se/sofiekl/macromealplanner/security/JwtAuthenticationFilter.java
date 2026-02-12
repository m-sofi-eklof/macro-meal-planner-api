package se.sofiekl.macromealplanner.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import se.sofiekl.macromealplanner.service.CustomUserDetailsService;
import se.sofiekl.macromealplanner.service.JwtService;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;

    public JwtAuthenticationFilter(
            JwtService jwtService,
            CustomUserDetailsService userDetailsService
    ) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        log.info("-- JWT FILTER -----");
        log.info("Path: {}", request.getRequestURI());
        log.info("Auth header is present: {}", authHeader != null);

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            log.info("Header starts with Bearer");
            String token = authHeader.substring(7);
            log.info("Token length: {}", token.length());

            try {
                boolean isValid = jwtService.isTokenValid(token);
                log.info("Token valid: {}", isValid);

                if (isValid) {
                    String username = jwtService.extractSubject(token);
                    log.info("Extracted username: {}", username);

                    if(username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                        log.info("UserDetails loaded: {}", userDetails.getUsername());

                        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );
                        authentication.setDetails(
                                new WebAuthenticationDetailsSource().buildDetails(request)
                        );
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                        log.info("Authentication setup success");
                    }
                }
            } catch (Exception e) {
                log.error("Error processing token {}", e.getMessage());
                e.printStackTrace();
            }
        }else {
            log.warn("No Bearer token found");
        }

        filterChain.doFilter(request, response);
    }
}
