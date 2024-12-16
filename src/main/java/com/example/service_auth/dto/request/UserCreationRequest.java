package com.example.service_auth.dto.request;

import com.example.service_auth.exception.ErrorCode;
import com.example.service_auth.validator.DobConstraint;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreationRequest {

    @Size(min = 5, message = "USERNAME_INVALID")
    String username;
    @Size(min = 6, message = "PASSWORD_INVALID")
    String password;
    String firstName;
    String lastName;

    @DobConstraint(min = 16, message = "INVALID_DOB")
    LocalDate dob;

    String roles;

}
