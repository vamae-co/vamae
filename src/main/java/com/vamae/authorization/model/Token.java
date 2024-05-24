package com.vamae.authorization.model;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "tokens")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Token {

    @Id
    @GeneratedValue
    private String id;
    @NotBlank(message = "JWT is required")
    @Indexed(unique = true)
    private String jwt;
    @Setter
    private boolean isValid;
    @NotNull
    private Date expirationDate;
}
