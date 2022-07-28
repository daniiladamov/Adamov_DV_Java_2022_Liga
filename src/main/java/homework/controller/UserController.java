package homework.controller;

import homework.entity.user.User;
import homework.entity.user.UserGetDto;
import homework.entity.user.UserSaveDto;
import homework.exception.EntityNotFoundException;
import homework.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v2/users")
public class UserController {
    private final UserService userService;
    private final ModelMapper modelMapper;
    @Value("${exception_message}")
    private String exceptionMessage;

    @PostMapping
    public ResponseEntity<Long> createUser(@Valid @RequestBody UserSaveDto userSaveDto){

        User user=modelMapper.map(userSaveDto,User.class);
        Long userId=userService.createUser(user);
        return new ResponseEntity<>(userId, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserGetDto> getUser(@PathVariable Long id) throws EntityNotFoundException {
        Optional<User> user = userService.getUser(id);
        if (user.isPresent()){
            UserGetDto userGetDto=modelMapper.map(user.get(),UserGetDto.class);
            return new ResponseEntity<>(userGetDto,HttpStatus.OK);
        }
        else {
            throw new EntityNotFoundException(
                    String.format(exceptionMessage,User.class.getSimpleName(),id));
        }
    }
    @PostMapping("/{id}")
    public ResponseEntity<UserGetDto> updateUser(@Valid @RequestBody UserSaveDto userSaveDto, @PathVariable Long id)
            throws EntityNotFoundException{
        User user=modelMapper.map(userSaveDto,User.class);
        user.setId(id);
        User userUpdate = userService.updateUser(user);
        UserGetDto userGetDto=modelMapper.map(userUpdate,UserGetDto.class);
        return new ResponseEntity<>(userGetDto,HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<UserGetDto> deleteUser(@PathVariable Long id) {
        User user=userService.deleteUser(id);
        UserGetDto userGetDto=modelMapper.map(user, UserGetDto.class);
        return new ResponseEntity<>(userGetDto, HttpStatus.OK);
    }
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> objectNotFoundResponse(EntityNotFoundException ex){
        return new ResponseEntity<>(ex.getMessage(),HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> totValidResponse(MethodArgumentNotValidException ex){
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
}
