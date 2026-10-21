package uk.gov.hmcts.reform.dev.tasks;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AccessLevel;
import java.time.OffsetDateTime;

@Entity
@Table(name = "tasks")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TaskStatus status;

    @Column(name = "due_date_time", nullable = false)
    private OffsetDateTime dueDateTime;

    public Task(
            String title,
            String description,
            TaskStatus status,
            OffsetDateTime dueDateTime) {

        this.title = title;
        this.description = description;
        this.status = status;
        this.dueDateTime = dueDateTime;
    }

    public void updateStatus(TaskStatus status) {
        this.status = status;
    }

}
