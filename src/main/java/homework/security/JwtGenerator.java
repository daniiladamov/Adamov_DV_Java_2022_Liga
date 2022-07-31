package homework.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.Date;

@Component
public class JwtGenerator {
    @Value("${jwt.secret_key}")
    private String secretKey;
    @Value("${jwt.subject}")
    private String subject;
    @Value("${jwt.life_time_hours}")
    private int lifeTime;
    @Value("${api_name}")
    private String apiName;

    public String generateToken(String username){
        Date tokenLifeCycle=
                Date.from(ZonedDateTime.now().plusHours(lifeTime).toInstant());
        return JWT.create()
                .withSubject(subject)
                .withClaim("username",username)
                .withIssuedAt(new Date())
                .withIssuer(apiName)
                .withExpiresAt(tokenLifeCycle)
                .sign(Algorithm.HMAC256(secretKey));
    }
//@todo: изменить параметры, которые возвращает метод (мапу, будем больше claims хранить в токене)
    public String validateJwtToken(String jwtToken) throws JWTVerificationException {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secretKey))
                .withSubject(subject)
                .withIssuer(apiName)
                .build();
        DecodedJWT decodedJWT = verifier.verify(jwtToken);
        return decodedJWT.getClaim("username").asString();
    }
}
