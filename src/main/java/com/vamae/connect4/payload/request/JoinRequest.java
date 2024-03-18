package com.vamae.connect4.payload.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class JoinRequest {

    private String secondPlayerId;
    private String gameId;
}
