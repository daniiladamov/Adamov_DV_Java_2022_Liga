package homework.util;

import homework.entity.user.User;
import homework.entity.user.UserSaveDto;
import homework.exception.LoginAlreadyUsedException;
import homework.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserValidator implements Validator {
    private final UserService userService;
    @Override
    public boolean supports(Class<?> clazz) {
        return UserSaveDto.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        UserSaveDto user= (UserSaveDto) target;
        Optional<User> userOptional=userService.getUserByLogin(user.getLogin());
        if (userOptional.isPresent()){
            errors.rejectValue("login","","Данный логин уже занят");
            throw new LoginAlreadyUsedException(String.format("логин %s уже используется",user.getLogin()));
        }
    }
}
