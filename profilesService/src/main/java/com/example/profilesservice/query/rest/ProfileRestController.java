package com.example.profilesservice.query.rest;

import com.example.profilesservice.core.data.ProfileRepository;
import com.example.profilesservice.query.FindProfileQuery;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/profiles")
public class ProfileRestController {
    private final QueryGateway queryGateway;
    public ProfileRestController(QueryGateway queryGateway) {
        this.queryGateway = queryGateway;
    }
    @GetMapping
    public List<ProfileRestModel> getProfiles() throws ExecutionException, InterruptedException {
        FindProfileQuery findProfileQuery = new FindProfileQuery();
        return queryGateway.query(findProfileQuery, ResponseTypes.multipleInstancesOf(ProfileRestModel.class)).get();
    }
}
