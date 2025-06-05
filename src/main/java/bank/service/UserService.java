package bank.service;

import bank.dao.AccountRepository;
import bank.dao.EmailDataRepository;
import bank.dao.PhoneDataRepository;
import bank.dao.UserRepository;
import bank.model.dto.UserCreateRequest;
import bank.model.dto.UserDTO;
import bank.model.entity.Account;
import bank.model.entity.EmailData;
import bank.model.entity.PhoneData;
import bank.model.entity.User;
import bank.service.converter.UserConverter;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final PhoneDataRepository phoneDataRepository;
    private final EmailDataRepository emailDataRepository;
    private final UserConverter userConverter;

    public List<UserDTO> getUserDTOs() {
        return getUsers().stream().map(userConverter::toDto).toList();
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public User getUserByPhoneOrEmail(String emailOrPhone) {
        var emailData = emailDataRepository.findByEmail(emailOrPhone);
        if (emailData != null) return emailData.getUser();

        var phoneData = phoneDataRepository.findByPhone(emailOrPhone);
        if (phoneData != null) return phoneData.getUser();

        return null;
    }

    /* --- its unused - just to confirm everything works --- */
    @Transactional
    @SuppressWarnings("unused")
    public User createUser(UserCreateRequest request) {
        User user = new User(request.getName(), request.getDateOfBirth(), request.getPassword());
        userRepository.save(user);

        accountRepository.save(new Account(user, request.getInitialDeposit()));
        phoneDataRepository.save(new PhoneData(user, request.getPhone()));
        emailDataRepository.save(new EmailData(user, request.getEmail()));

        return user;
    }
}
