package com.vamae.authorization.service;

import com.vamae.common.exception.TokenAlreadyExistsException;
import com.vamae.common.exception.TokenNotFoundException;
import com.vamae.authorization.model.Token;
import com.vamae.authorization.repository.TokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final TokenRepository tokenRepository;

    public Token createToken(String token, Date expirationDate) {
        if (!tokenRepository.existsByJwt(token)) {
            return tokenRepository.save(
                    Token.builder()
                            .jwt(token)
                            .isValid(true)
                            .expirationDate(expirationDate)
                            .build()
            );
        }
        throw new TokenAlreadyExistsException(String.format("Token [%s] already exists!", token));
    }

    public void removeInvalidTokens() {
        tokenRepository.deleteTokenByExpirationDateBefore(new Date());
    }

    public void invalidateToken(String token) {
        Token storedToken = getTokenInformation(token);
        storedToken.setValid(false);
        tokenRepository.save(storedToken);
    }

    public Token getTokenInformation(String token) {
        return tokenRepository.findTokenByJwt(token)
                .orElseThrow(() -> new TokenNotFoundException(String.format("Token [%s] not found", token)));
    }
}
