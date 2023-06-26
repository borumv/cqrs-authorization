package com.example.profilesservice.core.data;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "profiles")
public class ProfileEntity implements Serializable {
    @Id
    @Column(unique = true)
    private String id;
    private String userId;
    @Column
    private String nickName;
    private String firstName;
    private String lastName;
    private String avatarUrl;
    private String aboutDescription;
    private LocalDate dateOfRegistry;
}
