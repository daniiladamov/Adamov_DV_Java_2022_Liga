package homework.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;

    public void auth(String username, String password){
        UsernamePasswordAuthenticationToken authToken=
                new UsernamePasswordAuthenticationToken(username, password);
        authenticationManager.authenticate(authToken);
    }
}
