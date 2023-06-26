package com.example.profilesservice.query;

import com.example.profilesservice.core.data.ProfileEntity;
import com.example.profilesservice.core.data.ProfileRepository;
import com.example.profilesservice.query.rest.ProfileRestModel;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProfileQueryHandler {
    private final ProfileRepository profileRepository;
    public ProfileQueryHandler(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }
    @QueryHandler
    public List<ProfileRestModel> findAll(FindProfileQuery query){
        return profileRepository.findAll().stream()
                .map(storedProfile -> {
                    ProfileRestModel profileRestModel = new ProfileRestModel();
                    BeanUtils.copyProperties(storedProfile, profileRestModel);
                    return profileRestModel;
                })
                .collect(Collectors.toList());
    }

}
