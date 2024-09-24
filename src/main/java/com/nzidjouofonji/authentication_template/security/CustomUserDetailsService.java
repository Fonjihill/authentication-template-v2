package com.nzidjouofonji.authentication_template.security;


import com.nzidjouofonji.authentication_template.models.Utilisateur;
import com.nzidjouofonji.authentication_template.repository.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UtilisateurRepository utilisateurRepository;

    @Autowired
    public CustomUserDetailsService(UtilisateurRepository utilisateurRepository) {
        this.utilisateurRepository = utilisateurRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Utilisateur> user = utilisateurRepository.findByEmail(email);
            if(user.isPresent()) {
                Set<GrantedAuthority> authorities = user.get().getRoles().stream()
                        .map(role -> new SimpleGrantedAuthority(role.getName()))
                        .collect(Collectors.toSet());

                return new org.springframework.security.core.userdetails.User(user.get().getEmail(), user.get().getPassword(), authorities);
            }
        throw new UsernameNotFoundException("Utilisateur not found with email: " + email);
    }
}
