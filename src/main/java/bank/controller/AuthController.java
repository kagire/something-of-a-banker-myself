package bank.controller;

import bank.config.auth.JwtUtil;
import bank.model.dto.LoginInfo;
import bank.model.entity.User;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("auth")
@AllArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtils;

    @io.swagger.v3.oas.annotations.parameters.RequestBody(
        description = "Email change request",
        required = true,
        content = @Content(
            schema = @Schema(implementation = LoginInfo.class,
                example = """
                {
                   "emailOrPhone": "user1@test.com",
                   "password": "12345678"
                }
            """)
        ))
    @PostMapping("login")
    public String authenticateUser(@RequestBody LoginInfo loginInfo) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                loginInfo.getEmailOrPhone(),
                loginInfo.getPassword()
            )
        );
        User user = (User) authentication.getPrincipal();
        return jwtUtils.generateToken(String.valueOf(user.getId()));
    }
}
