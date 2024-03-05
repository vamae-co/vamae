package com.vamae.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.vamae.controller.Connect4;
import org.vamae.enums.Piece;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Connect4Game {
    @Id
    @GeneratedValue
    private Long id;
    private Long firstPlayerId;
    private Long secondPlayerId;
    private int betSum;
    private GameBoard game;
    private Piece currentPlayer;
}
