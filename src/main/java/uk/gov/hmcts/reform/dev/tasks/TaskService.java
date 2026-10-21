package uk.gov.hmcts.reform.dev.tasks;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

import uk.gov.hmcts.reform.dev.tasks.dto.*;

@Service
public class TaskService {

    private final TaskRepository repository;

    public TaskService(TaskRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public TaskResponse createTask(CreateTaskRequest request) {
        Task task = new Task(
            request.title(),
            request.description(),
            request.status(),
            request.dueDateTime()
        );

        return TaskResponse.from(repository.save(task));
    }

    @Transactional(readOnly = true)
    public TaskResponse getTask(Long id) {
        return TaskResponse.from(findTask(id));
    }

    @Transactional(readOnly = true)
    public List<TaskResponse> getAllTasks() {
        return repository.findAll()
            .stream()
            .map(TaskResponse::from)
            .toList();
    }

    @Transactional
    public TaskResponse updateStatus(
        Long id, UpdateTaskStatusRequest request) {

        Task task = findTask(id);
        task.updateStatus(request.status());

        return TaskResponse.from(task);
    }

    @Transactional
    public void deleteTask(Long id) {
        Task task = findTask(id);
        repository.delete(task);
    }

    private Task findTask(Long id) {
        return repository.findById(id)
            .orElseThrow(() -> new TaskNotFoundException(id));
    }
}
