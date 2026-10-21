package uk.gov.hmcts.reform.dev.tasks;

import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.bind.annotation.ResponseStatus;

import uk.gov.hmcts.reform.dev.tasks.dto.CreateTaskRequest;
import uk.gov.hmcts.reform.dev.tasks.dto.TaskResponse;
import uk.gov.hmcts.reform.dev.tasks.dto.UpdateTaskStatusRequest;



@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    public ResponseEntity<TaskResponse> createTask(
        @Valid @RequestBody CreateTaskRequest request) {

        TaskResponse created = taskService.createTask(request);

        return ResponseEntity
            .created(URI.create("/api/tasks/" + created.id()))
            .body(created);
    }

    @GetMapping
    public List<TaskResponse> getAllTasks() {
        return taskService.getAllTasks();
    }

    @GetMapping("/{id}")
    public TaskResponse getTask(@PathVariable Long id) {
        return taskService.getTask(id);
    }

    @PatchMapping("/{id}/status")
    public TaskResponse updateStatus(
        @PathVariable Long id,
        @Valid @RequestBody UpdateTaskStatusRequest request) {

        return taskService.updateStatus(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
    }
}
