package com.nzidjouofonji.authentication_template.request;


import jakarta.validation.constraints.*;
import lombok.Data;


@Data
public class Register {

    @NotBlank
    @NotNull
    private String username;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    @NotNull
    @Size(min = 8, max = 20)
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,20}$")
    private String password;
}
