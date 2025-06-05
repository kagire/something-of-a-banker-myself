package bank.config;

import lombok.AllArgsConstructor;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;

@Configuration
@AllArgsConstructor
public class DBConfig {

    private Environment env;

    @Bean
    public DataSource dataSource() {
        return DataSourceBuilder.create()
            .url(env.getProperty("db.url"))
            .driverClassName(env.getProperty("db.driver"))
            .username(env.getProperty("db.username"))
            .password(env.getProperty("db.password"))
            .build();
    }
}
