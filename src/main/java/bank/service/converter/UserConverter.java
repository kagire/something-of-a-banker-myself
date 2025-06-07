package bank.service.converter;

import bank.dao.AccountRepository;
import bank.dao.EmailDataRepository;
import bank.dao.PhoneDataRepository;
import bank.model.dto.UserDTO;
import bank.model.entity.EmailData;
import bank.model.entity.PhoneData;
import bank.model.entity.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@AllArgsConstructor
public class UserConverter {

    private final AccountRepository accountRepository;
    private final PhoneDataRepository phoneDataRepository;
    private final EmailDataRepository emailDataRepository;

    @Transactional
    public UserDTO toDto(User user) {
        return UserDTO.builder()
            .id(user.getId())
            .name(user.getName())
            .dateOfBirth(user.getDateOfBirth())
            .balance(accountRepository.findByUser(user).getBalance())
            .emails(emailDataRepository.findAllByUser(user).stream().map(EmailData::getEmail).toList())
            .phones(phoneDataRepository.findAllByUser(user).stream().map(PhoneData::getPhone).toList())
            .build();
    }
}
