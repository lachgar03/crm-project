package org.pfa.salesservice.security;

import jakarta.persistence.EntityManager;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

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

        try {
            final String authHeader = request.getHeader("Authorization");
            final String jwt;
            final String username;
            final Long tenantId;

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                filterChain.doFilter(request, response);
                return;
            }

            jwt = authHeader.substring(7);
            
            try {
                // simple validation just signature and expiration
                if (jwtService.isTokenValid(jwt)) {
                    username = jwtService.extractUsername(jwt);
                    tenantId = jwtService.extractTenantId(jwt);

                    // 1. Set Tenant Context
                    if (tenantId != null) {
                        TenantContextHolder.setTenantId(tenantId);

                        // 2. Enable Hibernate Filter
                        Session session = entityManager.unwrap(Session.class);
                        session.enableFilter("tenantFilter")
                                .setParameter("tenantId", tenantId);
                        
                        log.debug("Tenant filter enabled for tenantId: {}", tenantId);
                    }

                    // 3. Set Security Context (so @PreAuthorize works, if roles were extracted)
                    // For now, simple authenticated user without roles, unless we extract roles too
                    if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                                username,
                                null,
                                Collections.emptyList() // TODO: Extract roles if needed
                        );
                        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    }
                }
            } catch (Exception e) {
                 log.error("JWT Processing Error: {}", e.getMessage());
                 // Don't throw, just let the chain proceed (it will likely fail 403 downstream if auth missing)
            }

            filterChain.doFilter(request, response);

        } finally {
            TenantContextHolder.clear();
        }
    }
}
