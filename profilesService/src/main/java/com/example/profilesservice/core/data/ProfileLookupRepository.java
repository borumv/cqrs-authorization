package com.example.profilesservice.core.data;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileLookupRepository extends JpaRepository<ProfileLookupEntity, String> {
    ProfileLookupEntity findByNickNameOrUserId(String nickName, String userId);
}
