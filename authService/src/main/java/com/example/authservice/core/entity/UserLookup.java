package com.example.authservice.core.entity;

import jakarta.persistence.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "userLookup")
public class UserLookup {
    @Id
    private String userId;
    @Column
    private String email;


}
