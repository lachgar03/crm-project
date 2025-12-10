package org.pfa.salesservice.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.persistence.EntityManager;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final EntityManager entityManager;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        jwt = authHeader.substring(7);
        
        try {
            // simple validation check first
            if (jwtService.isTokenValid(jwt)) {
                userEmail = jwtService.extractUsername(jwt);
                Long tenantId = jwtService.extractTenantId(jwt);

                // 1. SET TENANT CONTEXT
                if (tenantId != null) {
                    TenantContextHolder.setTenantId(tenantId);
                    
                    // CRITICAL: Enable Hibernate Filter for Data Isolation
                    try {
                        Session session = entityManager.unwrap(Session.class);
                        session.enableFilter("tenantFilter").setParameter("tenantId", tenantId);
                    } catch (Exception e) {
                        log.error("Failed to enable tenant filter", e);
                    }
                }

                // 2. SET SECURITY CONTEXT (RBAC FIX)
                if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    
                    // Extract roles directly from JWT (Stateless - No DB call needed here)
                    List<String> roles = jwtService.extractClaim(jwt, claims -> claims.get("roles", List.class));
                    
                    List<SimpleGrantedAuthority> authorities = (roles != null) 
                            ? roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList())
                            : Collections.emptyList();

                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userEmail,
                            null,
                            authorities
                    );

                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }

        } catch (Exception e) {
            log.error("Authentication failed: {}", e.getMessage());
            // Don't throw exceptions here to allow public endpoints to work if configured
        }

        try {
            filterChain.doFilter(request, response);
        } finally {
            // ALWAYS CLEAR CONTEXT
            TenantContextHolder.clear();
        }
    }
}
