package homework.security;

import com.auth0.jwt.exceptions.JWTVerificationException;
import homework.service.JwtGeneratorService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final String jwtPrefix="Bearer ";
    private final JwtGeneratorService jwtGeneratorService;
    private final UserDetailsService detailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String authorization = request.getHeader("Authorization");

        if (Objects.nonNull(authorization) && authorization.startsWith(jwtPrefix)){
            String jwt=authorization.replace(jwtPrefix,"");
            if (jwt.isBlank()){
                throw new JWTVerificationException("верификация jwt-токена не пройдена");
            }
            else {
                Pair<String, String> jwtInfo = jwtGeneratorService.validateJwtAccessToken(jwt);
                CustomUserDetails userDetails = (CustomUserDetails) detailsService.loadUserByUsername(jwtInfo.getFirst());
                if (jwtInfo.getSecond().equals(userDetails.getUser().getUuid())) {
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(userDetails,
                                    userDetails.getPassword(),
                                    userDetails.getAuthorities());
                    if (SecurityContextHolder.getContext().getAuthentication() == null) {
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    }
                }
            }
        }
        filterChain.doFilter(request,response);
    }
}
