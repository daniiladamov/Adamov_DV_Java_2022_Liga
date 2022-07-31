package homework.security;

import com.auth0.jwt.exceptions.JWTVerificationException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    @Value("${jwt.prefix}")
    private String jwtPrefix;
    private final JwtGenerator jwtGenerator;
    private final UserDetailsService detailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String authorization = request.getHeader("Authorization");

        if (authorization.startsWith(jwtPrefix)){
            String jwt=authorization.replace(jwtPrefix,"");
            if (jwt.isBlank()){
//                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "невалидный jwt-токен в заголовке " +
//                        "запроса");
                throw new JWTVerificationException("верификация не пройдена");
            }
            else {
                String userName = jwtGenerator.validateJwtToken(jwt);
                UserDetails userDetails=detailsService.loadUserByUsername(userName);
                UsernamePasswordAuthenticationToken authToken=
                        new UsernamePasswordAuthenticationToken(userDetails,
                                userDetails.getPassword(),
                                userDetails.getAuthorities());
                if (SecurityContextHolder.getContext().getAuthentication()==null){
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }

            }
        }
        filterChain.doFilter(request,response);
    }
}
