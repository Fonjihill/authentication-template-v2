package com.nzidjouofonji.authentication_template.mapper;

import com.nzidjouofonji.authentication_template.dto.UtilisateurDTO;
import com.nzidjouofonji.authentication_template.models.Utilisateur;
import com.nzidjouofonji.authentication_template.request.Register;

public class UserMapper {

    public static UtilisateurDTO toUserDTO(Utilisateur utilisateur) {
        return UtilisateurDTO.builder()
                .firstName(utilisateur.getFirstName())
                .lastName(utilisateur.getLastName())
                .username(utilisateur.getUsername())
                .email(utilisateur.getEmail())
                .build();
    }

    public static Utilisateur toUser(UtilisateurDTO utilisateurDTO) {
        return Utilisateur.builder()
                .id(utilisateurDTO.getId())
                .firstName(utilisateurDTO.getFirstName())
                .lastName(utilisateurDTO.getLastName())
                .username(utilisateurDTO.getUsername())
                .email(utilisateurDTO.getEmail())
                .build();
    }


    public static Utilisateur mapRegisterDto(Register register) {
        return Utilisateur.builder()
                .email(register.getEmail())
                .lastName(register.getLastName())
                .firstName(register.getFirstName())
                .username(register.getUsername())
                .password(register.getPassword())

                .build();

    }
}
