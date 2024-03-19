package com.vamae.poker.model;

import com.vamae.poker.lib.models.dto.TableDto;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

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
