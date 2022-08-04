package homework.service;

import homework.entity.Project;
import homework.entity.User;
import homework.exception.EntityNotFoundException;
import homework.repository.ProjectRepo;
import homework.util.CustomPage;
import homework.util.Specifications;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProjectService {
    private final ProjectRepo projectRepo;
    @Value("${exception_message}")
    private String exceptionMessage;

    public Page<Project> getProjects(@NonNull CustomPage customPage) {
        Sort sort = Sort.by(customPage.getSortDirection(), customPage.getSortBy());
        Pageable pageable = PageRequest.of(customPage.getPageNumber(), customPage.getPageSize(), sort);
        return projectRepo.findAll(pageable);
    }

    @Transactional
    public Long createProject(@NonNull Project project) {
        Project savedProject = projectRepo.save(project);
        return savedProject.getId();
    }

    public Project getProject(@NonNull Long id) {
        return projectRepo.findById(id).orElseThrow(() ->
                new EntityNotFoundException(String.format(exceptionMessage,
                        Project.class.getSimpleName(), id)));

    }

    @Transactional
    public Project updateProject(Project project) {
        Optional<Project> projectInBd = projectRepo.findById(project.getId());
        if (projectInBd.isPresent()) {
            Project updateProject = projectRepo.save(project);
            return updateProject;
        } else throw new EntityNotFoundException(String.format(exceptionMessage,
                Project.class.getSimpleName(), project.getId()));
    }

    @Transactional
    public Project deleteProject(Long id) {
        Project projectInBd = projectRepo.findById(id).orElseThrow(() ->
                new EntityNotFoundException(String.format(exceptionMessage, Project.class.getSimpleName(), id)));
        projectRepo.delete(projectInBd);
        return projectInBd;
    }

    private Page<Project> getProjectsByUser(User user, Pageable pageable) {
        return projectRepo.findAll(Specifications.getUserProjects(user), pageable);
    }
    public Page<Project> getUserProjects(User user, Pageable pageable) {
            return getProjectsByUser(user, pageable);
    }
}
