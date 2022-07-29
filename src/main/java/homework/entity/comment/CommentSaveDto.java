package homework.entity.comment;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@Getter
@Setter
public class CommentSaveDto {
    @NotBlank
    private String content;
}
