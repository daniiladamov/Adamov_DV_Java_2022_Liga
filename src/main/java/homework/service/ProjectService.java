package homework.service;

import homework.entity.Project;
import homework.entity.user.User;
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

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProjectService {
    @PersistenceContext
    private final EntityManager entityManager;
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


    public Optional<Project> getProject(@NonNull Long id) {
        return projectRepo.findById(id);
    }
    @Transactional
    public Project updateProject(Project project) {
        Optional<Project> projectInBd=projectRepo.findById(project.getId());
        if (projectInBd.isPresent()){
            Project updateProject = projectRepo.save(project);
            return updateProject;
        }
        else throw new EntityNotFoundException(String.format(exceptionMessage,
                Project.class.getSimpleName(),project.getId()));
    }
    private Project deleteProject(Long id) {
        Optional<Project> projectInBd=projectRepo.findById(id);
        if (projectInBd.isPresent()){
            projectRepo.delete(projectInBd.get());
            return projectInBd.get();
        }
        else throw new EntityNotFoundException(String.format(exceptionMessage,
                Project.class.getSimpleName(),id));
    }

    private Page<Project> getProjectsByUser(User user, Pageable pageable){
        return projectRepo.findAll(Specifications.getUserProjects(user),pageable);
    }
    @Transactional
    public void deleteProject(Optional<Project> projectOptional, Long projectId) {
        if (projectOptional.isPresent()){
            entityManager.createNativeQuery("delete from project_user where project_id= :id")
                    .setParameter("id",projectId).executeUpdate();
            deleteProject(projectId);
        }
        else
            throw new EntityNotFoundException(
                    String.format(exceptionMessage,Project.class.getSimpleName(),projectId));
    }
    public Page<Project> getUserProjects(Optional<User> userOptional, Long id, CustomPage customPage) {
        if (userOptional.isPresent()){
            Sort sort = Sort.by(customPage.getSortDirection(), customPage.getSortBy());
            Pageable pageable = PageRequest.of(customPage.getPageNumber(), customPage.getPageSize(), sort);
            return getProjectsByUser(userOptional.get(),pageable);
        }
        else
            throw new EntityNotFoundException(
                    String.format(exceptionMessage,User.class.getSimpleName(), id));
    }
}
