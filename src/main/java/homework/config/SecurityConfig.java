package homework.config;

import homework.security.JwtFilter;
import homework.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter
{

   private final CustomUserDetailsService detailsService;
   private final JwtFilter jwtFilter;
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(detailsService).
                passwordEncoder(getPasswordEncoder());
    }
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.
                csrf().disable().
                authorizeRequests().
                antMatchers(HttpMethod.POST,"/v2/users").permitAll().
                anyRequest().authenticated().
                and().
                formLogin().defaultSuccessUrl("/swagger-ui/#").
                and().
                httpBasic(Customizer.withDefaults()).
                logout().logoutUrl("/logout").logoutSuccessUrl("/login").
                and().
                sessionManagement().
                sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
    }
    @Bean
    public PasswordEncoder getPasswordEncoder(){
        return new BCryptPasswordEncoder(8);
    }
}
