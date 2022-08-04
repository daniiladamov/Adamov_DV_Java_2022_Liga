package homework.service;

import homework.dto.security.UserAppDto;
import homework.entity.User;
import homework.mapper.UserMapper;
import homework.repository.UserRepo;
import homework.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepo userRepo;
    private final UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findByLogin(username).orElseThrow(()->
        new UsernameNotFoundException(String.format("Пользователь с логином %s не найден",
                username)));
            UserAppDto userApp=userMapper.toAppEntity(user);
            return new CustomUserDetails(userApp);
    }
}
