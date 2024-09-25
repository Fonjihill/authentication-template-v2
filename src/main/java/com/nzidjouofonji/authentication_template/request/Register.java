package com.nzidjouofonji.authentication_template.request;


import jakarta.validation.constraints.*;
import lombok.Data;


@Data
public class Register {

    @NotBlank
    @NotNull
    @Size(min = 4, max = 10)
    private String username;

    @NotBlank
    private String lastName;

    @NotBlank
    private String firstName;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    @NotNull
    @Size(min = 8, max = 20)
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,20}$")
    private String password;
}
