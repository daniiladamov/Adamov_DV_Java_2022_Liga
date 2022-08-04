package homework.controller;

import homework.dto.AuthDto;
import homework.dto.JwtRefresh;
import homework.dto.JwtResponse;
import homework.service.JwtGeneratorService;
import homework.service.UserService;
import lombok.RequiredArgsConstructor;
import org.h2.security.auth.AuthenticationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v2/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtGeneratorService jwtGeneratorService;
    private final UserService userService;

    @PostMapping
    public JwtResponse loginUser(@Validated @RequestBody AuthDto authDto) throws AuthenticationException {
        UsernamePasswordAuthenticationToken authToken=
                new UsernamePasswordAuthenticationToken(authDto.getUsername(), authDto.getPassword());
        authenticationManager.authenticate(authToken);
        JwtResponse jwtResponse = jwtGeneratorService.generateTokens(authToken.getName());
        return jwtResponse;
    }
    @PostMapping("/jwt-access")
    public String getAccessTokenByRefresh(@RequestBody JwtRefresh jwtRefresh){
        String username = jwtGeneratorService.validateJwtRefreshToken(jwtRefresh.getRefreshToken());
        return jwtGeneratorService.generateJwtAccessToken(username);
    }
    @PostMapping("jwt-refresh")
    public JwtResponse refreshAllTokens(@RequestBody JwtRefresh jwtRefresh) throws AuthenticationException {
        String username= jwtGeneratorService.validateJwtRefreshToken(jwtRefresh.getRefreshToken());
        JwtResponse jwtResponse = jwtGeneratorService.generateTokens(username);
        return jwtResponse;
    }
}
