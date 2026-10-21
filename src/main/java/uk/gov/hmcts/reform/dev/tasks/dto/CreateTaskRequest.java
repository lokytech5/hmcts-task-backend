
package uk.gov.hmcts.reform.dev.tasks.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import uk.gov.hmcts.reform.dev.tasks.TaskStatus;

import java.time.OffsetDateTime;

public record CreateTaskRequest(
    @NotBlank
    @Size(max = 150)
    String title,

    String description,

    @NotNull
    TaskStatus status,

    @NotNull
    OffsetDateTime dueDateTime
) {}
