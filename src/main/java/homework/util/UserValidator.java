package homework.util;

import homework.entity.user.User;
import homework.entity.user.UserSaveDto;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class UserValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return UserSaveDto.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        User user= (User) target;
        errors.rejectValue("","","");
    }
}
