package com.nzidjouofonji.authentication_template.controller;


import com.nzidjouofonji.authentication_template.dto.UtilisateurDTO;
import com.nzidjouofonji.authentication_template.request.Login;
import com.nzidjouofonji.authentication_template.request.Register;
import com.nzidjouofonji.authentication_template.response.AccessTokenResponse;
import com.nzidjouofonji.authentication_template.security.JwtTokenProvider;
import com.nzidjouofonji.authentication_template.services.IUtilisateurService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/api/auth")
public class AuthenticationController {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

    private final IUtilisateurService utilisateurService;

    private final JwtTokenProvider tokenProvider;

    public AuthenticationController(IUtilisateurService utilisateurService, JwtTokenProvider tokenProvider) {
        this.utilisateurService = utilisateurService;
        this.tokenProvider = tokenProvider;
    }


    @PostMapping(value = "/register")
    public ResponseEntity<?> createAccount(@RequestBody Register registerRequest) {

        try{
            UtilisateurDTO result = utilisateurService.register(registerRequest);
            return ResponseEntity.ok(result);
        }
        catch(IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

    }


    @PostMapping(value="/login")
    public ResponseEntity<?> loginAccount(@RequestBody Login loginRequest){

        try{
            String token = utilisateurService.login(loginRequest);
            return ResponseEntity.ok(new AccessTokenResponse(token));
        }
        catch (BadCredentialsException e){
            return ResponseEntity.status(HttpStatus.LOCKED).body(e.getMessage());
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser(@RequestHeader("Authorization") String token) {

        String jwt = token.substring(7);
        tokenProvider.revokeToken(jwt);

        return ResponseEntity.ok("Logged out successfully");
    }
}
