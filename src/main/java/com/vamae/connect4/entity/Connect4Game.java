package com.vamae.connect4.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import com.vamae.connect4.lib.controller.Connect4;
import com.vamae.connect4.lib.enums.Piece;

@Document
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Connect4Game {

    @Id
    @GeneratedValue
    private String id;
    private String firstPlayerId;
    private String secondPlayerId;
    private int betSum;
    @Setter
    private Connect4 game;
    @Setter
    private Piece currentPlayer;
    @Setter
    private boolean isWinFlag;
}
