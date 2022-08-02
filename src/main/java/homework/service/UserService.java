package homework.service;

import homework.entity.Project;
import homework.entity.user.User;
import homework.exception.EntityNotFoundException;
import homework.repository.UserRepo;
import homework.util.CustomPage;
import homework.util.Specifications;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.h2.security.auth.AuthenticationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    @PersistenceContext
    private final EntityManager entityManager;
    @Value("${exception_message}")
    private String exceptionMessage;
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    @PostAuthorize("hasRole('ADMIN') || " +
            "(returnObject.isPresent() && returnObject.get().login==authentication.name)")
    public Optional<User> getUser(@NonNull Long id) {
        return userRepo.findById(id);
    }
    @Transactional
    public Long createUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepo.save(user);
        return savedUser.getId();
    }
    @Transactional
    public User updateUser(User user) {
        Optional<User> userInBd=userRepo.findById(user.getId());
        if (userInBd.isPresent()){
            User oldUser=userInBd.get();
            oldUser.setFirstName(user.getFirstName());
            oldUser.setLastName(user.getLastName());
            oldUser.setSurname(user.getSurname());
            oldUser.setLogin(user.getLogin());
            oldUser.setPassword(passwordEncoder.encode(user.getPassword()));
            User updateUser = userRepo.save(oldUser);
            return updateUser;
        }
        else throw new EntityNotFoundException(String.format(exceptionMessage,
                User.class.getSimpleName(),user.getId()));
    }
    private User deleteUser(Long id) {
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

    private Page<User> getUsersByProject(Project project, Pageable pageable) {
        return userRepo.findAll(Specifications.getProjectUsers(project),pageable);
    }

    public Optional<User> getUserByLogin(String login){
        return userRepo.findByLogin(login);
    }
    @Transactional
    public void deleteUser(Optional<User> userOptional, Long userId){

        if (userOptional.isPresent()){
            entityManager.createNativeQuery("delete from project_user where user_id= :id")
                    .setParameter("id",userId).executeUpdate();
            deleteUser(userId);
        }
        else
            throw new EntityNotFoundException(
                    String.format(exceptionMessage,User.class.getSimpleName(),userId));
    }

    public Page<User> getProjectUsers(Optional<Project> projectOptional, Long id, CustomPage customPage) {
        if (projectOptional.isPresent()){
            Sort sort = Sort.by(customPage.getSortDirection(), customPage.getSortBy());
            Pageable pageable = PageRequest.of(customPage.getPageNumber(), customPage.getPageSize(), sort);
            return getUsersByProject(projectOptional.get(),pageable);
        }
        else
            throw new EntityNotFoundException(
                    String.format(exceptionMessage,Project.class.getSimpleName(), id));
    }
    @Transactional
    public void updateJwtTokenDate(long time, String login) throws AuthenticationException {
        Optional<User> userOptional=userRepo.findByLogin(login);
        if (userOptional.isPresent()){
            User user=userOptional.get();
            user.setRefreshDate(time);
            userRepo.save(user);
        }
        else
            throw new AuthenticationException();

    }
}
