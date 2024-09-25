package com.nzidjouofonji.authentication_template.dto;


import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UtilisateurDTO {

    private Integer id;

    private String firstName;

    private String lastName;

    private String username;

    private String email;


}
