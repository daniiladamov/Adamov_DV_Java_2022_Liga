package homework.config;

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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
//@Configuration
//@EnableGlobalMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter
{

   private final CustomUserDetailsService detailsService;
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(detailsService).
                passwordEncoder(getPasswordEncoder());
    }
//
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.
                csrf().disable().
                authorizeRequests().
                antMatchers(HttpMethod.POST,"/v2/users").anonymous().
//                antMatchers("v2/users/{id}","v2/tasks/{id}").hasAnyRole("USER","ADMIN").
                antMatchers("/**").hasRole("ADMIN").


//                anyRequest().authenticated().
                and().
                formLogin().defaultSuccessUrl("/swagger-ui/#").
                and().
                httpBasic(Customizer.withDefaults()).
                logout().logoutUrl("/logout").logoutSuccessUrl("/login");
    }
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http)throws Exception{
//        return http.
//        csrf().disable().
//                authorizeRequests().
//                antMatchers(HttpMethod.POST,"/v2/users").anonymous().
//                antMatchers("v2/users/**","v2/tasks/**").hasAnyRole("USER","ADMIN").
//                antMatchers("/v2/**").hasRole("ADMIN").
//                and().
//                formLogin().defaultSuccessUrl("/swagger-ui/#").
////                and().
////                httpBasic().
//                and().
//                logout().logoutUrl("/logout").logoutSuccessUrl("/login").and().build();
//    }

    @Bean
    public PasswordEncoder getPasswordEncoder(){
        return new BCryptPasswordEncoder(8);
    }
}
