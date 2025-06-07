package bank.controller;

import bank.controller.exception.CurrentAuthHolder;
import bank.model.dto.MoneyTransferRequest;
import bank.model.dto.UserDTO;
import bank.model.entity.User;
import bank.service.MoneyOperationService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("account")
@AllArgsConstructor
public class AccountController implements CurrentAuthHolder {

    private final MoneyOperationService moneyService;

    @io.swagger.v3.oas.annotations.parameters.RequestBody(
        description = "Money transfer request",
        required = true,
        content = @Content(
            schema = @Schema(implementation = MoneyTransferRequest.class,
                example = """
                {
                    "userId": 4781035953051993873,
                    "amount": 0.15
                }
            """)
        )
    )
    @PostMapping("transfer")
    public UserDTO transfer(@RequestBody MoneyTransferRequest transferRequest) {
        User user = getCurrentUser();
        return moneyService.transfer(transferRequest.getAmount(), user, transferRequest.getUserId());
    }
}
