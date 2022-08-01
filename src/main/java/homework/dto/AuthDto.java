package homework.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@NoArgsConstructor
@Getter
@Setter
public class AuthDto {
    @NotBlank
    @Size(min = 4)
    private String username;
    @NotBlank
    @Size(min = 8)
    private String password;
}
