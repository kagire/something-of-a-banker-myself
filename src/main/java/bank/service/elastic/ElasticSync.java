package bank.service.elastic;

import bank.dao.EmailDataRepository;
import bank.dao.PhoneDataRepository;
import bank.dao.UserRepository;
import bank.dao.elastic.UserElRepository;
import bank.model.entity.EmailData;
import bank.model.entity.PhoneData;
import bank.model.entity.User;
import bank.model.entity.elastic.UserEl;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class ElasticSync {

    private final UserRepository userRepository;
    private final EmailDataRepository emailDataRepository;
    private final PhoneDataRepository phoneDataRepository;
    private final UserElRepository userElRepository;

    @Transactional
    @Scheduled(fixedRate = 60_000)
    public void updateBalances() {
        List<User> users = userRepository.findAll();

        List<UserEl> elUsers = users.stream()
            .map(user -> {
                List<String> phones = phoneDataRepository.findAllByUser(user).stream()
                    .map(PhoneData::getPhone)
                    .toList();
                List<String> emails = emailDataRepository.findAllByUser(user).stream()
                    .map(EmailData::getEmail)
                    .toList();

                return new UserEl(
                    user.getId(),
                    user.getName(),
                    user.getDateOfBirth(),
                    phones, emails
                );
            }).collect(Collectors.toList());

        userElRepository.saveAll(elUsers);
        log.info("Synced {} accounts to Elasticsearch", elUsers.size());
    }
}
