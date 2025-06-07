package bank.controller;

import bank.controller.exception.CurrentAuthHolder;
import bank.model.dto.ChangeValueRequest;
import bank.model.dto.UserDTO;
import bank.model.dto.ValueRequest;
import bank.model.entity.User;
import bank.service.UserService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("user")
@AllArgsConstructor
public class UserController implements CurrentAuthHolder {

    private final UserService userService;

    @GetMapping("all")
    public List<UserDTO> getUsers() {
        return userService.getUserDTOs();
    }

    @GetMapping("current")
    public UserDTO getUser() {
        User user = getCurrentUser();
        return userService.getUserDTO(user.getId());
    }

    @io.swagger.v3.oas.annotations.parameters.RequestBody(
        description = "Email change request",
        required = true,
        content = @Content(
            schema = @Schema(implementation = ChangeValueRequest.class,
                example = """
                {
                  "prevValue": "user1@test.com",
                  "newValue": "user1.1@test.com"
                }
            """)
        )
    )
    @PatchMapping("email/change")
    public UserDTO changeEmail(@RequestBody ChangeValueRequest request) {
        User user = getCurrentUser();
        return userService.changeEmail(user, request);
    }

    @io.swagger.v3.oas.annotations.parameters.RequestBody(
        description = "Email add request",
        required = true,
        content = @Content(schema = @Schema(implementation = ValueRequest.class, example = "new.email@test.com")))
    @PostMapping("email/add")
    public UserDTO addEmail(@RequestBody ValueRequest email) {
        User user = getCurrentUser();
        return userService.addEmail(user, email.getValue());
    }

    @io.swagger.v3.oas.annotations.parameters.RequestBody(
        description = "Email delete request",
        required = true,
        content = @Content(schema = @Schema(implementation = ValueRequest.class, example = "new.email@test.com")))
    @DeleteMapping("email/delete")
    public UserDTO deleteEmail(@RequestBody ValueRequest email) {
        User user = getCurrentUser();
        return userService.deleteEmail(user, email.getValue());
    }

    @PatchMapping("phone/change")
    public UserDTO changePhone(@RequestBody ChangeValueRequest request) {
        User user = getCurrentUser();
        return userService.changePhone(user, request);
    }

    @io.swagger.v3.oas.annotations.parameters.RequestBody(
        description = "Phone add request",
        required = true,
        content = @Content(schema = @Schema(implementation = ValueRequest.class, example = "375170987654")))
    @PostMapping("phone/add")
    public UserDTO addPhone(@RequestBody ValueRequest phone) {
        User user = getCurrentUser();
        return userService.addPhone(user, phone.getValue());
    }

    @io.swagger.v3.oas.annotations.parameters.RequestBody(
        description = "Phone delete request",
        required = true,
        content = @Content(schema = @Schema(implementation = ValueRequest.class, example = "375170987654")))
    @DeleteMapping("phone/delete")
    public UserDTO deletePhone(@RequestBody ValueRequest phone) {
        User user = getCurrentUser();
        return userService.deletePhone(user, phone.getValue());
    }
}
