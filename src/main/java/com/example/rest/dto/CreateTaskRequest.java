package com.example.rest.dto;

import com.example.domain.model.Task;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CreateTaskRequest {

    @NotBlank(message = "Task is required")
    private String text;
    private LocalDateTime dateTime;

    public static CreateTaskRequest fromEntity(Task task) {
        var request = new CreateTaskRequest();
        request.setText(task.getText());
        request.setDateTime(task.getDateTime());
        return request;
    }

    }


