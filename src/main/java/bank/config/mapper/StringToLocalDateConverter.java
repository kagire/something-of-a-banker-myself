package bank.config.mapper;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

@Component
public class StringToLocalDateConverter implements Converter<String, LocalDate> {

    private final List<DateTimeFormatter> formatters = List.of(
        DateTimeFormatter.ofPattern("yyyy-MM-dd"),
        DateTimeFormatter.ofPattern("dd.MM.yyyy")
    );

    @Override
    public LocalDate convert(String source) {
        for (DateTimeFormatter formatter : formatters) {
            try {
                return LocalDate.parse(source, formatter);
            } catch (DateTimeParseException ignored) {}
        }
        throw new IllegalArgumentException("Invalid date format: " + source);
    }
}
