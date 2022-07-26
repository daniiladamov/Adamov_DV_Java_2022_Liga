package homework.entity.project;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ProjectDto {
    private Long id;
    private String title;
    private String description;
}
