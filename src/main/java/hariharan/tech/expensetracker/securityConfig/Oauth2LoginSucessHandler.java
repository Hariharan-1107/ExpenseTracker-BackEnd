package hariharan.tech.expensetracker.securityConfig;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import hariharan.tech.expensetracker.services.JWTService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

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
        response.sendRedirect("https://expense-tracker-front-end-sage.vercel.app/?token=" + token );
    }

}
