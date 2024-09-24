package com.nzidjouofonji.authentication_template.mapper;

import com.nzidjouofonji.authentication_template.dto.UserDTO;
import com.nzidjouofonji.authentication_template.models.Utilisateur;

public class UserMapper {

    public static UserDTO toUserDTO(Utilisateur utilisateur) {
        return UserDTO.builder()
                .id(utilisateur.getId())
                .firstName(utilisateur.getFirstName())
                .lastName(utilisateur.getLastName())
                .username(utilisateur.getUsername())
                .email(utilisateur.getEmail())
                .build();
    }

    public static Utilisateur toUser(UserDTO userDTO) {
        return Utilisateur.builder()
                .id(userDTO.getId())
                .firstName(userDTO.getFirstName())
                .lastName(userDTO.getLastName())
                .username(userDTO.getUsername())
                .email(userDTO.getEmail())
                .build();
    }
}
