package homework.util.validator;

import homework.entity.User;
import homework.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Valid;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class LoginValidator implements ConstraintValidator<UniqueLogin, String>
{
    private final UserService userService;

    @Override
    public void initialize(UniqueLogin constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(@Valid  String login, ConstraintValidatorContext context) {
        Optional<User> userOptional=userService.getUserByLogin(login);
        if (userOptional.isPresent())
            return false;
        return true;
    }
}
