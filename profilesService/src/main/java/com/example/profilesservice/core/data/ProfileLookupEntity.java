package com.example.profilesservice.core.data;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "profilelookup")
public class ProfileLookupEntity implements Serializable {

    @Id
    private String userId;
    @Column
    private String nickName;
}
