package com.vamae.connect4.payload.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class InitializationRequest {

    private int columns;
    private int rows;
    private String firstPlayerId;
    private int bet;
}
