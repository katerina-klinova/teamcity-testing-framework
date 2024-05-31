package com.example.teamcity.api.models;

import lombok.*;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private String username;
    private String password;
    private String email;
    private Roles roles;
    private String id;
    private String href;
    private Properties properties;
    private Groups groups;
}
