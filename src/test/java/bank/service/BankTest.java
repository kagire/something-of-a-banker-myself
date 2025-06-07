package bank.service;

import bank.dao.AccountRepository;
import bank.dao.UserRepository;
import bank.model.entity.User;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Testcontainers
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = "spring.profiles.active=test")
class BankTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private MoneyOperationService moneyOperationService;

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:17")
        .withDatabaseName("testdb")
        .withUsername("testuser")
        .withPassword("testpass");

    @DynamicPropertySource
    static void overrideProps(DynamicPropertyRegistry registry) {
        registry.add("db.url", postgres::getJdbcUrl);
        registry.add("db.username", postgres::getUsername);
        registry.add("db.password", postgres::getPassword);
        registry.add("db.driver", postgres::getDriverClassName);
    }

    @Test
    void testGetUser() throws Exception {
        String token = mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                       "emailOrPhone": "user1@test.com",
                       "password": "12345678"
                    }
                  """))
            .andExpect(status().isOk())
            .andExpect(content().string(Matchers.not(Matchers.blankString())))
            .andReturn()
            .getResponse()
            .getContentAsString();

        mockMvc.perform(get("/user/current")
            .header("Authorization", "Bearer " + token))
            .andExpect(status().isOk());
    }

    @Test
    void testMoneyTransfer() {
        List<User> users = userRepository.findAll();

        if (users.size() < 2) return;

        User from = users.stream()
            .filter(user -> "user1".equals(user.getName()))
            .findFirst().orElseThrow();
        User to = users.stream()
            .filter(user -> "user2".equals(user.getName()))
            .findFirst().orElseThrow();

        BigDecimal initialFrom = accountRepository.findByUser(from).getBalance();
        BigDecimal initialTo = accountRepository.findByUser(to).getBalance();

        moneyOperationService.transfer(BigDecimal.valueOf(0.1), from, to.getId());

        BigDecimal afterFrom = accountRepository.findByUser(from).getBalance();
        BigDecimal afterTo = accountRepository.findByUser(to).getBalance();

        Assertions.assertTrue(initialFrom.compareTo(afterFrom) > 0);
        Assertions.assertTrue(initialTo.compareTo(afterTo) < 0);
    }
}