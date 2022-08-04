package homework.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ProjectGetDto {
    private Long id;

    private String title;

    private String description;
}
