package com.vamae.connect4.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.vamae.controller.Connect4;
import org.vamae.enums.Piece;

@Document
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Connect4Game {
    @Id
    @GeneratedValue
    private String id;
    private Long firstPlayerId;
    private Long secondPlayerId;
    private int betSum;
    @Setter
    private Connect4 game;
    @Setter
    private Piece currentPlayer;
}
