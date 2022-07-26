package homework.controller;

import homework.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v2/tasks")
public class TaskController {
    private final TaskService taskService;
    private final ModelMapper modelMapper;
}
