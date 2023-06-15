package models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class UserData {

    Integer id;
    String email;
    @JsonProperty(value = "first_name")
    String firstName;
    @JsonProperty(value = "last_name")
    String lastName;
    String avatar;
}
