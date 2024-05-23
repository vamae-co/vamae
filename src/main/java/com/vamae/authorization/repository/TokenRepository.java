package com.vamae.authorization.repository;

import com.vamae.authorization.model.Token;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Date;
import java.util.Optional;

public interface TokenRepository extends MongoRepository<Token, String> {

    boolean existsByJwt(String jwt);
    void deleteTokenByExpirationDateBefore(Date expirationDate);
    Optional<Token> findTokenByJwt(String jwt);
}
