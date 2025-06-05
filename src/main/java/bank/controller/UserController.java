package bank.controller;

import bank.model.dto.UserDTO;
import bank.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("user")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("all")
    public List<UserDTO> getUsers() {
        return userService.getUserDTOs();
    }
}
