package uk.gov.hmcts.reform.dev.tasks.dto;

import jakarta.validation.constraints.NotNull;
import uk.gov.hmcts.reform.dev.tasks.TaskStatus;

public record UpdateTaskStatusRequest(
    @NotNull TaskStatus status
) {}
