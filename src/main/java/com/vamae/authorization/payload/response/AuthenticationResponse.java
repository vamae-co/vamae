package com.vamae.authorization.payload.response;

import lombok.Builder;

@Builder
public record AuthenticationResponse (
        String token
) {
}
