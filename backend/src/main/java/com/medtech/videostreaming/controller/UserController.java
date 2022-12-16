package com.medtech.videostreaming.controller;

import com.medtech.videostreaming.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/users")
@RequiredArgsConstructor
public class UserController {

    final private UserService userService;


    @PostMapping
    public String registerUser(Authentication authentication){
        Jwt jwt = (Jwt) authentication.getPrincipal();
        userService.registerUser(jwt.getTokenValue());
        return "The user has saved successfuly";
    }

    @PostMapping(value = "/subscribe/{userid}")
    @ResponseStatus(HttpStatus.OK)
    public String subscribeUser(@PathVariable String userid){
        return userService.subscribeToUser(userid);

    }



}
