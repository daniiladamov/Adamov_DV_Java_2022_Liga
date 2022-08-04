package homework.service;

import homework.entity.Project;
import homework.entity.User;
import homework.exception.EntityNotFoundException;
import homework.repository.UserRepo;
import homework.util.CustomPage;
import homework.util.Specifications;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
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

    @PostAuthorize("hasRole('ADMIN') || (returnObject.login==authentication.name)")
    public User getUser(@NonNull Long id) {
        return userRepo.findById(id).orElseThrow(() ->
                new EntityNotFoundException(String.format(exceptionMessage, User.class.getSimpleName(), id)));
    }
    @Transactional
    public Long createUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepo.save(user);
        return savedUser.getId();
    }
    @Transactional
    public User updateUser(User user) {
        User oldUser = userRepo.findById(user.getId()).orElseThrow(() ->
                new EntityNotFoundException(String.format(exceptionMessage,
                        User.class.getSimpleName(), user.getId())));
        oldUser.setFirstName(user.getFirstName());
        oldUser.setLastName(user.getLastName());
        oldUser.setSurname(user.getSurname());
        oldUser.setLogin(user.getLogin());
        oldUser.setPassword(passwordEncoder.encode(user.getPassword()));
        User updateUser = userRepo.save(oldUser);
        return updateUser;
    }
    @Transactional
    public void deleteUser(Long id) {
        User user = getUser(id);
        entityManager.createNativeQuery("delete from project_user where user_id= :id")
                .setParameter("id",id).executeUpdate();
        userRepo.delete(user);
    }
    public Page<User> getUsers(CustomPage customPage) {
        Sort sort = Sort.by(customPage.getSortDirection(), customPage.getSortBy());
        Pageable pageable = PageRequest.of(customPage.getPageNumber(), customPage.getPageSize(), sort);
        return userRepo.findAll(pageable);
    }
    private Page<User> getUsersByProject(Project project, Pageable pageable) {
        return userRepo.findAll(Specifications.getProjectUsers(project), pageable);
    }
    public Optional<User> getUserByLogin(String login) {
        return userRepo.findByLogin(login);
    }
    public Page<User> getProjectUsers(Project project, Long id, Pageable pageable) {
        return getUsersByProject(project, pageable);
    }
}
