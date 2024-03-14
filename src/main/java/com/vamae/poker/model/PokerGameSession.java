package com.vamae.poker.model;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.vamae.models.dto.TableDto;

import java.util.Map;

@Document
@Data
@Builder
public class PokerGameSession {

    @Id
    @GeneratedValue
    private String id;
    private TableDto table;
    private Map<String, String> playersLinks;
}
