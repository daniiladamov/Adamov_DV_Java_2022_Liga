package homework.dto.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class JwtRefresh {
    @NotBlank
    private String refreshToken;
}
