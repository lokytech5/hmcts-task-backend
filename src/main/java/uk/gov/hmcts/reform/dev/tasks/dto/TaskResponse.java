package uk.gov.hmcts.reform.dev.tasks.dto;

import java.time.OffsetDateTime;
import uk.gov.hmcts.reform.dev.tasks.Task;
import uk.gov.hmcts.reform.dev.tasks.TaskStatus;

public record TaskResponse(
    Long id,
    String title,
    String description,
    TaskStatus status,
    OffsetDateTime dueDateTime
) {
    public static TaskResponse from(Task task) {
        return new TaskResponse(
            task.getId(),
            task.getTitle(),
            task.getDescription(),
            task.getStatus(),
            task.getDueDateTime()
        );
    }
}
