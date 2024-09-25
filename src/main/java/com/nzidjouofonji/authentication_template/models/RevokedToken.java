package com.nzidjouofonji.authentication_template.models;


import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "revoked_tokens")
public class RevokedToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "token", nullable = false, unique = true, columnDefinition = "TEXT")
    private String token;

    @Column(name = "expiration_date", nullable = false)
    private Date expirationDate;

    public RevokedToken() {}

    public RevokedToken(String token, Date expirationDate) {
        this.token = token;
        this.expirationDate = expirationDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }
}
