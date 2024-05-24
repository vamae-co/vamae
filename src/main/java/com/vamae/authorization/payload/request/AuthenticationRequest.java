package com.vamae.authorization.payload.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record AuthenticationRequest (
        @NotBlank(message = "Username cannot be empty")
        String username,
        @NotBlank(message = "Password cannot be empty")
        String password
) {
}
