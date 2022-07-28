package homework.controller;

import homework.entity.project.Project;
import homework.entity.project.ProjectGetDto;
import homework.entity.project.ProjectSaveDto;
import homework.exception.EntityNotFoundException;
import homework.service.ProjectService;
import homework.util.CustomPage;
import homework.util.DtoMapper;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v2/projects")
public class ProjectController {
    private final ProjectService projectService;
    private final ModelMapper modelMapper;
    private final DtoMapper dtoMapper;
    @Value("${exception_message}")
    private String exceptionMessage;

    @GetMapping
    public ResponseEntity<Page<ProjectGetDto>> getProjects(CustomPage customPage){
        Page<Project> projects=projectService.getProjects(customPage);
        return new ResponseEntity<>(dtoMapper.mapToPage(projects,ProjectGetDto.class), HttpStatus.OK);
    }
    @PostMapping
    public ResponseEntity<Long> createProject(@Valid @RequestBody ProjectSaveDto projectSaveDto){
        Project project=modelMapper.map(projectSaveDto,Project.class);
        Long userId=projectService.createProject(project);
        return new ResponseEntity<>(userId, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectGetDto> getUser(@PathVariable Long id) throws EntityNotFoundException {
        Optional<Project> project= projectService.getProject(id);
        if (project.isPresent()){
            ProjectGetDto projectGetDto=modelMapper.map(project.get(),ProjectGetDto.class);
            return new ResponseEntity<>(projectGetDto,HttpStatus.OK);
        }
        else {
            throw new EntityNotFoundException(
                    String.format(exceptionMessage,Project.class.getSimpleName(),id));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProjectGetDto> updateUser(@Valid @RequestBody ProjectSaveDto projectSaveDto, @PathVariable Long id)
            throws EntityNotFoundException{
        Project project=modelMapper.map(projectSaveDto,Project.class);
        project.setId(id);
        Project projectUpdate = projectService.updateProject(project);
        ProjectGetDto projectGetDto=modelMapper.map(projectUpdate,ProjectGetDto.class);
        return new ResponseEntity<>(projectGetDto,HttpStatus.OK);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<ProjectGetDto> deleteUser(@PathVariable Long id)
            throws EntityNotFoundException{
        Project project =projectService.deleteUser(id);
        ProjectGetDto projectGetDto=modelMapper.map(project, ProjectGetDto.class);
        return new ResponseEntity<>(projectGetDto, HttpStatus.OK);
    }

}
