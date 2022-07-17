package homework.service;

import homework.entity.user.User;
import homework.repository.UserRepo;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepo userRepo;

    public Optional<User> getUser(@NonNull Long id) {
        return userRepo.findById(id);
    }
}
