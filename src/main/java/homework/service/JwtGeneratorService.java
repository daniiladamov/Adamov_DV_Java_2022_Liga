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

@Service
@RequiredArgsConstructor
public class JwtGeneratorService {
    private final UserRepo userRepo;
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

    public String generateToken(String username,String secretKey, int lifeTime, Date date){
        Date tokenLifeCycle=
                Date.from(ZonedDateTime.now().plusMinutes(lifeTime).toInstant());
        return JWT.create()
                .withSubject(subject)
                .withClaim("username",username)
                .withIssuedAt(date)
                .withIssuer(apiName)
                .withExpiresAt(tokenLifeCycle)
                .sign(Algorithm.HMAC256(secretKey));
    }

    public JwtResponse generateTokens(String username, Date date){
        String accessToken=generateToken(username,secretAccessKey, lifeTimeAcces,date);
        String refreshToken=generateToken(username,secretRefreshKey, lifeTimeRefresh,date);
        return new JwtResponse(accessToken,refreshToken);
    }

    public String generateJwtAccessToken(String username){
        return generateToken(username,secretAccessKey, lifeTimeAcces, new Date());
    }
    public Pair<String, Date> validateJwtAccessToken(String jwtToken){
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secretAccessKey))
                .withSubject(subject)
                .withIssuer(apiName)
                .build();
        DecodedJWT decodedJWT = verifier.verify(jwtToken);
        Date issuedAt = decodedJWT.getIssuedAt();
        String username = decodedJWT.getClaim("username").asString();
        return Pair.of(username,issuedAt);

    }
    public String validateJwtRefreshToken(String jwtToken){
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secretRefreshKey))
                .withSubject(subject)
                .withIssuer(apiName)
                .build();
        DecodedJWT decodedJWT = verifier.verify(jwtToken);
        String username = decodedJWT.getClaim("username").asString();
        Date issuedAt = decodedJWT.getIssuedAt();
        Optional<User> byLogin = userRepo.findByLogin(username);
        if(byLogin.isPresent()){
            long date=byLogin.get().getRefreshDate();
            if (date==issuedAt.getTime())
                return username;
            else
                throw new JWTVerificationException("");
        }
        throw new JWTVerificationException("");

    }
}
