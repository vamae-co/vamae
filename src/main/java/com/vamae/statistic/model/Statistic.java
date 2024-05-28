package com.vamae.statistic.model;

import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Statistic {

    @Id
    private String id;
    private String username;
    private int authCount;
}
