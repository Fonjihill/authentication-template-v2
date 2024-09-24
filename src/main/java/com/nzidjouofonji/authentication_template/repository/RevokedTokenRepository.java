package com.nzidjouofonji.authentication_template.repository;


import com.nzidjouofonji.authentication_template.models.RevokedToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RevokedTokenRepository extends JpaRepository<RevokedToken, Long> {

    Optional<RevokedToken> findByToken(String token);
}
