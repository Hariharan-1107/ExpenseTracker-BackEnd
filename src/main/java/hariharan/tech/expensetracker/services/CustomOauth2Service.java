package hariharan.tech.expensetracker.services;

import hariharan.tech.expensetracker.models.User;
import hariharan.tech.expensetracker.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class CustomOauth2Service extends DefaultOAuth2UserService {

    @Autowired
    UserRepo userRepo;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        System.out.println(oAuth2User.getAttributes());

        String name = oAuth2User.getAttribute("name");
        String email = oAuth2User.getAttribute("email");
        String picture = oAuth2User.getAttribute("picture");


        User user = userRepo.findByEmail(email).map(existingUser -> {
            if(!existingUser.getName().equals(name)){
                existingUser.setName(name);
            }
            if(!existingUser.getPicture().equals(picture)){
                existingUser.setPicture(picture);
            }
            return existingUser;
        }).orElseGet(() -> User.builder()
                .email(email)
                .name(name)
                .picture(picture)
                .build());
        System.out.println("User before Saving" + user);
        userRepo.save(user);

        return oAuth2User;

    }

}
