package homework.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import homework.dto.JwtResponse;
import homework.entity.user.User;
import homework.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.modelmapper.internal.Pair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class JwtGeneratorService {
    private final UserRepo userRepo;
    @Value("${jwt.secret_access_key}")
    private String secretAccessKey;
    @Value("${jwt.secret_refresh_key}")
    private String secretRefreshKey;
    @Value("${jwt.subject}")
    private String subject;
    @Value("${jwt.life_time_access}")
    private int lifeTimeAcces;
    @Value("${jwt.life_time_refresh}")
    private int lifeTimeRefresh;
    @Value("${api_name}")
    private String apiName;

    public String generateToken(String username,String secretKey, int lifeTime, String uuid){
        User user = userRepo.findByLogin(username).orElse(null);
        user.setUuid(uuid);
        userRepo.save(user);
        Date tokenLifeCycle=
                Date.from(ZonedDateTime.now().plusMinutes(lifeTime).toInstant());
        return JWT.create()
                .withSubject(subject)
                .withClaim("username",username)
                .withClaim("uuid",uuid)
                .withIssuedAt(new Date())
                .withIssuer(apiName)
                .withExpiresAt(tokenLifeCycle)
                .sign(Algorithm.HMAC256(secretKey));
    }

    public JwtResponse generateTokens(String username){
        String uuid= UUID.randomUUID().toString();
        String accessToken=generateToken(username,secretAccessKey, lifeTimeAcces,uuid);
        String refreshToken=generateToken(username,secretRefreshKey, lifeTimeRefresh,uuid);
        return new JwtResponse(accessToken,refreshToken);
    }

    public String generateJwtAccessToken(String username){
        User user = userRepo.findByLogin(username).orElse(null);
        return generateToken(username,secretAccessKey, lifeTimeAcces, user.getUuid());
    }
    public Pair<String, String> validateJwtAccessToken(String jwtToken){
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secretAccessKey))
                .withSubject(subject)
                .withIssuer(apiName)
                .build();
        DecodedJWT decodedJWT = verifier.verify(jwtToken);
        String uuid = decodedJWT.getClaim("uuid").asString();
        String username = decodedJWT.getClaim("username").asString();
        return Pair.of(username,uuid);

    }
    public String validateJwtRefreshToken(String jwtToken){
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secretRefreshKey))
                .withSubject(subject)
                .withIssuer(apiName)
                .build();
        DecodedJWT decodedJWT = verifier.verify(jwtToken);
        String username = decodedJWT.getClaim("username").asString();
        String jwtUuid = decodedJWT.getClaim("uuid").asString();
        Optional<User> byLogin = userRepo.findByLogin(username);
        if(byLogin.isPresent()){
            String uuid=byLogin.get().getUuid();
            if (uuid.equals(jwtUuid))
                return username;
            else
                throw new JWTVerificationException("");
        }
        throw new JWTVerificationException("");

    }
}
