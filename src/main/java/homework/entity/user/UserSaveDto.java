package homework.entity.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class UserSaveDto {
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    private String surname;
    @NotBlank
    @Size(min = 6)
    private String login;
    @NotBlank
    @Size(min = 8)
    private String password;
}