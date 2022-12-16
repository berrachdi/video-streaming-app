package com.medtech.videostreaming.config;

import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.Jwt;

public class AudienceValidator implements OAuth2TokenValidator<Jwt> {
    private String audience;

    public AudienceValidator(String audience) {
        this.audience = audience;
    }

    public AudienceValidator(Object p0) {
    }

    @Override
    public OAuth2TokenValidatorResult validate(Jwt token) {
        if(token.getAudience().contains(audience)){
            return OAuth2TokenValidatorResult.success();
        }
        return OAuth2TokenValidatorResult.failure(new OAuth2Error("Invalid audience token!"));
    }
}
