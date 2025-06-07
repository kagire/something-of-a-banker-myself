package bank.service;

import bank.dao.AccountRepository;
import bank.dao.EmailDataRepository;
import bank.dao.PhoneDataRepository;
import bank.dao.UserRepository;
import bank.model.dto.ChangeValueRequest;
import bank.model.dto.UserCreateRequest;
import bank.model.dto.UserDTO;
import bank.model.entity.Account;
import bank.model.entity.EmailData;
import bank.model.entity.PhoneData;
import bank.model.entity.User;
import bank.service.converter.UserConverter;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final PhoneDataRepository phoneDataRepository;
    private final EmailDataRepository emailDataRepository;
    private final UserConverter userConverter;

    @Cacheable(value = "userDTO", key = "#p0")
    public UserDTO getUserDTO(long id) {
        return userConverter.toDto(getUser(id));
    }

    @Cacheable(value = "userDTOs")
    public List<UserDTO> getUserDTOs() {
        return getUsers().stream().map(userConverter::toDto).toList();
    }

    @Transactional
    @CacheEvict(value = {"userDTO", "userDTOs"}, allEntries = true)
    public UserDTO changeEmail(User user, ChangeValueRequest request) {
        EmailData emailData = Optional
            .ofNullable(emailDataRepository.findByEmail(request.getPrevValue()))
            .orElseThrow(() -> new IllegalStateException("Specified email absent!"));

        if (emailDataRepository.findByEmail(request.getNewValue()) != null)
            throw new IllegalArgumentException("Email already taken!");

        emailData.setEmail(request.getNewValue());
        emailDataRepository.save(emailData);
        return userConverter.toDto(user);
    }

    @Transactional
    @CacheEvict(value = {"userDTO", "userDTOs"}, allEntries = true)
    public UserDTO addEmail(User user, String email) {
        if (emailDataRepository.findByEmail(email) != null)
            throw new IllegalArgumentException("Email already taken!");

        emailDataRepository.save(new EmailData(user, email));
        return userConverter.toDto(user);
    }

    @Transactional
    @CacheEvict(value = {"userDTO", "userDTOs"}, allEntries = true)
    public UserDTO deleteEmail(User user, String email) {
        List<EmailData> emails = emailDataRepository.findAllByUser(user);

        EmailData emailData = emails.stream()
            .filter(ed -> ed.getEmail().equals(email))
            .findFirst()
            .orElseThrow(() -> new IllegalStateException("Specified email absent!"));

        if (emails.size() < 2)
            throw new IllegalStateException("Cannot delete email - no other emails present!");

        emailDataRepository.delete(emailData);
        return userConverter.toDto(user);
    }

    @Transactional
    @CacheEvict(value = {"userDTO", "userDTOs"}, allEntries = true)
    public UserDTO changePhone(User user, ChangeValueRequest request) {
        PhoneData phoneData = Optional
            .ofNullable(phoneDataRepository.findByPhone(request.getPrevValue()))
            .orElseThrow(() -> new IllegalStateException("Specified phone absent!"));

        if (phoneDataRepository.findByPhone(request.getNewValue()) != null)
            throw new IllegalArgumentException("Phone already taken!");

        phoneData.setPhone(request.getNewValue());
        phoneDataRepository.save(phoneData);
        return userConverter.toDto(user);
    }

    @Transactional
    @CacheEvict(value = {"userDTO", "userDTOs"}, allEntries = true)
    public UserDTO addPhone(User user, String phone) {
        if (phoneDataRepository.findByPhone(phone) != null)
            throw new IllegalArgumentException("Phone already taken!");

        phoneDataRepository.save(new PhoneData(user, phone));
        return userConverter.toDto(user);
    }

    @Transactional
    @CacheEvict(value = {"userDTO", "userDTOs"}, allEntries = true)
    public UserDTO deletePhone(User user, String phone) {
        List<PhoneData> emails = phoneDataRepository.findAllByUser(user);

        PhoneData phoneData = emails.stream()
            .filter(ed -> ed.getPhone().equals(phone))
            .findFirst()
            .orElseThrow(() -> new IllegalStateException("Specified phone absent!"));

        if (emails.size() < 2)
            throw new IllegalStateException("Cannot delete phone - no other phones present!");

        phoneDataRepository.delete(phoneData);
        return userConverter.toDto(user);
    }

    private List<User> getUsers() {
        return userRepository.findAll();
    }

    private User getUser(long id) {
        return userRepository.getReferenceById(id);
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
