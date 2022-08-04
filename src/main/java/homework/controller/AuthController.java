package homework.controller;

import homework.dto.request.AuthDto;
import homework.dto.request.JwtRefresh;
import homework.dto.response.JwtResponse;
import homework.service.AuthService;
import homework.service.JwtGeneratorService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v2/auth")
public class AuthController {
    private final JwtGeneratorService jwtGeneratorService;
    private final AuthService authService;

    @PostMapping("/login")
    public JwtResponse loginUser(@Validated @RequestBody AuthDto authDto){
        authService.auth(authDto.getUsername(), authDto.getPassword());
        return jwtGeneratorService.generateTokens(authDto.getUsername());
    }
    @PostMapping("jwt-refresh")
    public JwtResponse refreshAllTokens(@RequestBody JwtRefresh jwtRefresh){
        String username= jwtGeneratorService.validateJwtRefreshToken(jwtRefresh.getRefreshToken());
        return jwtGeneratorService.generateTokens(username);
    }
}
