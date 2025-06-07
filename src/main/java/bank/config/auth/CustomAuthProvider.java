package bank.config.auth;

import bank.model.entity.User;
import bank.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Component
@AllArgsConstructor
public class CustomAuthProvider implements AuthenticationProvider {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String emailOrPhone = Optional.of(authentication)
            .map(Principal::getName)
            .orElseThrow(() -> new BadCredentialsException("no login present!"));
        String specifiedPassword = Optional.of(authentication)
            .map(Authentication::getCredentials)
            .map(Object::toString)
            .orElseThrow(() -> new BadCredentialsException("no password present!"));

        User user = userService.getUserByPhoneOrEmail(emailOrPhone);

        if (user != null) {
            String password = user.getPassword();
            if (!passwordEncoder.matches(specifiedPassword, password))
                throw new BadCredentialsException("wrong password!");

            Authentication authToken = new UsernamePasswordAuthenticationToken(
                user, password, List.of()
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);

            return authToken;
        } else {
            throw new BadCredentialsException("no such user!");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}