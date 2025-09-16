package hariharan.tech.expensetracker.securityConfig;

import com.fasterxml.jackson.databind.ObjectMapper;
import hariharan.tech.expensetracker.models.User;
import hariharan.tech.expensetracker.repos.UserRepo;
import hariharan.tech.expensetracker.services.JWTService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import org.hibernate.Hibernate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JWTService jwtService;
    private final ObjectMapper objectMapper;
    private final UserRepo userRepo;
    public JwtFilter(JWTService jwtService , ObjectMapper objectMapper, UserRepo userRepo) {
        this.jwtService = jwtService;
        this.objectMapper = objectMapper;
        this.userRepo = userRepo;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        String token = null;
        String email = null;
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
            if(jwtService.isTokenExpired(token))
            {
                handleExpiredToken(response);
                return;
            }
            email = jwtService.extractSubject(token);
            System.out.println("Email: " + email);
        }

        if(email != null && SecurityContextHolder.getContext().getAuthentication() == null)
        {
            User user = userRepo.findByEmail(email).orElse(null);
            if (user != null && jwtService.validateToken(token, user)) {
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(user, null, Collections.emptyList());

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request, response);
    }

    private void handleExpiredToken(HttpServletResponse response) throws IOException {

        System.out.println("Token expired");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");

        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", "TOKEN_EXPIRED");
        errorResponse.put("message", "JWT token has expired");

        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}
