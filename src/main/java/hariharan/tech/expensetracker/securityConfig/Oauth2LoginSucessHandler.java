package hariharan.tech.expensetracker.securityConfig;

import hariharan.tech.expensetracker.services.JWTService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class Oauth2LoginSucessHandler implements AuthenticationSuccessHandler {

    @Autowired
    JWTService jwtService;


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        String email = oAuth2User.getAttribute("email");
        String token = jwtService.generateToken(email);
        System.out.println("Token generated: " + token);
        response.sendRedirect("http://localhost:3004/?token=" + token );
    }

}
