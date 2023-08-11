package ua.foxminded.muzychenko.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UserRegistrationRequest {
    private final String email;
    private final String password;
    private final String repeatPassword;
    private final String firstName;
    private final String lastName;
}