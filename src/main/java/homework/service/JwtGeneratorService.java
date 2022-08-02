package homework.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import homework.dto.JwtResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.Date;

@Service
public class JwtGeneratorService {
    @Value("${jwt.secret_access_key}")
    private String secretAccessKey;
    @Value("{jwt.secret_refresh_key}")
    private String secretRefreshKey;
    @Value("${jwt.subject}")
    private String subject;
    @Value("${jwt.life_time_access}")
    private int lifeTimeAcces;
    @Value("${jwt.life_time_refresh}")
    private int lifeTimeRefresh;
    @Value("${api_name}")
    private String apiName;

    public String generateToken(String username,String secretKey, int lifeTime){
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
    public String validateJwtToken(String jwtToken,String secretKey) throws JWTVerificationException {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secretKey))
                .withSubject(subject)
                .withIssuer(apiName)
                .build();
        DecodedJWT decodedJWT = verifier.verify(jwtToken);
        return decodedJWT.getClaim("username").asString();
    }

    public JwtResponse generateTokens(String username){
        String accessToken=generateToken(username,secretAccessKey, lifeTimeAcces);
        String refreshToken=generateToken(username,secretRefreshKey, lifeTimeRefresh);
        return new JwtResponse(accessToken,refreshToken);
    }

    public String generateJwtAccessToken(String username){
        return generateToken(username,secretAccessKey, lifeTimeAcces);
    }
    public String validateJwtAccessToken(String jwtToken){
        return validateJwtToken(jwtToken,secretAccessKey);
    }
    public String validateJwtRefreshToken(String jwtToken){
        return validateJwtToken(jwtToken,secretRefreshKey);
    }
}
