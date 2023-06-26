package com.example.profilesservice.query.rest;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ProfileRestModel {
    private String userId;
    private String nickName;
    private String firstName;
    private String lastName;
    private String avatarUrl;
    private String aboutDescription;
    private LocalDate dateOfRegistry;
}
