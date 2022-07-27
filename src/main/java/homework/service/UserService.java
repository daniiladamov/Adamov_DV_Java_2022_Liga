package homework.service;

import homework.entity.user.User;
import homework.exception.EntityNotFoundException;
import homework.repository.UserRepo;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    @Value("${exception_message}")
    private String exceptionMessage;
    private final UserRepo userRepo;

    public Optional<User> getUser(@NonNull Long id) {
        return userRepo.findById(id);
    }

    public Long createUser(User user) {
        User savedUser = userRepo.save(user);
        return savedUser.getId();
    }

    public User updateUser(User user) {
        Optional<User> userInBd=userRepo.findById(user.getId());
        if (userInBd.isPresent()){
            User updateUser = userRepo.save(user);
            return updateUser;
        }
        else throw new EntityNotFoundException(String.format(exceptionMessage,
                User.class.getSimpleName(),user.getId()));
    }

    public User deleteUser(Long id) {
        Optional<User> userInBd=userRepo.findById(id);
        if (userInBd.isPresent()){
            userRepo.delete(userInBd.get());
            return userInBd.get();
        }
        else throw new EntityNotFoundException(String.format(exceptionMessage,
                User.class.getSimpleName(),id));
    }
}
