package com.nzidjouofonji.authentication_template.services;

import com.nzidjouofonji.authentication_template.dto.UtilisateurDTO;

import com.nzidjouofonji.authentication_template.request.Login;
import com.nzidjouofonji.authentication_template.request.Register;

public interface IUtilisateurService {

    UtilisateurDTO register(Register registration);

    String login(Login loginRequest);
}
