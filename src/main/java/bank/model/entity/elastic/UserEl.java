package bank.model.entity.elastic;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Getter
@Document(indexName = "user")
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserEl implements Serializable {

    @Id
    @Field(type = FieldType.Keyword)
    private Long id;

    @Field(type = FieldType.Keyword)
    private String name;

    @Field(type = FieldType.Date, format = DateFormat.date)
    private LocalDate dateOfBirth;

    @Field(type = FieldType.Keyword)
    List<String> phones;

    @Field(type = FieldType.Keyword)
    List<String> emails;
}
