package homework.service;

import homework.entity.user.User;
import homework.exception.EntityNotFoundException;
import homework.repository.UserRepo;
import homework.util.CustomPage;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    @Value("${exception_message}")
    private String exceptionMessage;
    private final UserRepo userRepo;

    public Optional<User> getUser(@NonNull Long id) {
        return userRepo.findById(id);
    }
    @Transactional
    public Long createUser(User user) {
        User savedUser = userRepo.save(user);
        return savedUser.getId();
    }
    @Transactional
    public User updateUser(User user) {
        Optional<User> userInBd=userRepo.findById(user.getId());
        if (userInBd.isPresent()){
            User updateUser = userRepo.save(user);
            return updateUser;
        }
        else throw new EntityNotFoundException(String.format(exceptionMessage,
                User.class.getSimpleName(),user.getId()));
    }
    @Transactional
    public User deleteUser(Long id) {
        Optional<User> userInBd=userRepo.findById(id);
        if (userInBd.isPresent()){
            userRepo.delete(userInBd.get());
            return userInBd.get();
        }
        else throw new EntityNotFoundException(String.format(exceptionMessage,
                User.class.getSimpleName(),id));
    }

    public Page<User> getUsers(CustomPage customPage) {
        Sort sort = Sort.by(customPage.getSortDirection(), customPage.getSortBy());
        Pageable pageable = PageRequest.of(customPage.getPageNumber(), customPage.getPageSize(), sort);
        return userRepo.findAll(pageable);
    }
}
