package com.example.profilesservice.core.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileRepository extends JpaRepository<ProfileEntity, String> {
    ProfileEntity findByUserId(String id);
    ProfileEntity findByNickName(String nickName);
}
