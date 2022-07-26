package homework.controller;

import homework.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v2/projects")
public class ProjectController {
    private final ProjectService projectService;
    private final ModelMapper modelMapper;
}
