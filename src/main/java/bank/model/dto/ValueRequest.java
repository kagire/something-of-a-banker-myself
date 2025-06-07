package bank.model.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ValueRequest {

    private String value;

    @JsonCreator
    public ValueRequest(Object value) {
        this.value = value.toString();
    }
}
