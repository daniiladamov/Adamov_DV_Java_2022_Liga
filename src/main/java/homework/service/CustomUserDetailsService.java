package homework.service;

import homework.entity.user.User;
import homework.repository.UserRepo;
import homework.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userOptional = userRepo.findByLogin(username);
        if (userOptional.isPresent()){
            return new CustomUserDetails(userOptional.get());
        }
        else
            throw new UsernameNotFoundException(String.format("Пользователь с логином %s не найден",
                    username));
    }
}
