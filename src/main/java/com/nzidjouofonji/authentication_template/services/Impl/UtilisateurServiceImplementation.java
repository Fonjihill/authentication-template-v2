package com.nzidjouofonji.authentication_template.services.Impl;


import com.nzidjouofonji.authentication_template.dto.UtilisateurDTO;
import com.nzidjouofonji.authentication_template.exceptions.AlreadyExistException;
import com.nzidjouofonji.authentication_template.mapper.UserMapper;
import com.nzidjouofonji.authentication_template.models.EntitiesRoleName;
import com.nzidjouofonji.authentication_template.models.Role;
import com.nzidjouofonji.authentication_template.models.Utilisateur;
import com.nzidjouofonji.authentication_template.repository.RoleRepository;
import com.nzidjouofonji.authentication_template.repository.UtilisateurRepository;
import com.nzidjouofonji.authentication_template.request.Login;
import com.nzidjouofonji.authentication_template.request.Register;
import com.nzidjouofonji.authentication_template.security.JwtTokenProvider;
import com.nzidjouofonji.authentication_template.security.LoginAttemptService;
import com.nzidjouofonji.authentication_template.services.IUtilisateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class UtilisateurServiceImplementation implements IUtilisateurService {

    private final UtilisateurRepository utilisateurRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtTokenProvider jwtTokenProvider;

    private final LoginAttemptService loginAttemptService;

    private final AuthenticationManager authenticationManager;


    @Autowired
    public UtilisateurServiceImplementation(UtilisateurRepository utilisateurRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider, LoginAttemptService loginAttemptService, AuthenticationManager authenticationManager) {
        this.utilisateurRepository = utilisateurRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.loginAttemptService = loginAttemptService;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public UtilisateurDTO register(Register registration) {

        Utilisateur user = UserMapper.mapRegisterDto(registration);

        Optional<Utilisateur> existingUser = utilisateurRepository.findByEmail(user.getEmail());
        if(existingUser.isPresent()){
            throw new AlreadyExistException("User already exists");
        }

       user.setPassword(passwordEncoder.encode(user.getPassword()));

        Role userRole = roleRepository.findByName(EntitiesRoleName.ROLE_USER)
                .orElseThrow(()-> new RuntimeException("Role not found"));

        Set<Role> roles = new HashSet<>();
        roles.add(userRole);
        user.setRoles(roles);

        utilisateurRepository.save(user);

        return UserMapper.toUserDTO(user);
    }

    @Override
    public String login(Login loginRequest) {

        String usernameOrEmail = loginRequest.getEmail();

        //Verifier si le compte est bloqué
        if(loginAttemptService.isBlocked(usernameOrEmail)) {
            throw new IllegalStateException("Your account has been blocked due to multiple failed login attempts.");
        }

        try{
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
            loginAttemptService.loginSucceeded(usernameOrEmail);
            return jwtTokenProvider.generateAccessToken(authentication);
        }catch(AuthenticationException e){

            //Tentative authentication echouée
            loginAttemptService.loginFailed(usernameOrEmail);
            throw new BadCredentialsException("Invalid Email or password");
        }
    }
}
