package com.vamae.poker.model.requests;

import com.vamae.poker.model.enums.Action;

public record MoveRequest(String id, Action action, int amount) {
}
