package com.nzidjouofonji.authentication_template.request;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Login {


    @Email
    @NotBlank
    private String email;

    @NotEmpty
    @NotBlank
    private String password;


}
