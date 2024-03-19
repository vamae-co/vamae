package com.vamae.connect4.payload.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MoveRequest {

    private int columnIndex;
    private String gameId;
}
