package homework.security;

import homework.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
@RequiredArgsConstructor
public class AuthProviderImpl implements AuthenticationProvider {
    private final CustomUserDetailsService userDetailsService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        String password=authentication.getCredentials().toString();
        if (password.endsWith(userDetails.getPassword())){
            return new UsernamePasswordAuthenticationToken(userDetails,password,
                    Collections.emptyList());
        }
        else
            throw new BadCredentialsException("Некорректный пароль");

    }

    @Override
    public boolean supports(Class<?> authentication) {
        return true;
    }
}
