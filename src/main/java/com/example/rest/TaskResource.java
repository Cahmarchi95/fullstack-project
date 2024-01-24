package com.example.rest;

import com.example.domain.model.Task;
import com.example.domain.repository.TaskRepository;
import com.example.rest.dto.CreateTaskRequest;
import com.example.rest.dto.ResponseError;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.time.LocalDateTime;
import java.util.Set;

@Path("/tasks")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class TaskResource {
    private final TaskRepository taskRepository;
    private final Validator validator;

    @Inject
    public TaskResource(TaskRepository taskRepository, Validator validator) {

        this.taskRepository = taskRepository;
        this.validator = validator;
    }


    @GET
    public Response getAllTasks() {

        PanacheQuery<Task> query = taskRepository.find("ORDER BY dateTime DESC");
        return Response.ok(query.list()).build();
    }

    @POST
    @Transactional
    public Response createTask(CreateTaskRequest taskRequest){


        Set<ConstraintViolation<CreateTaskRequest>> violations = validator.validate(taskRequest);
        if(!violations.isEmpty()){

            return ResponseError.createFromValidation(violations).withStatusCode(ResponseError.UNPROCESSABLE_ENTITY_STATUS);

        }

        Task task = new Task();
        task.setText(taskRequest.getText());
        task.setDateTime(taskRequest.getDateTime());

        taskRepository.persist(task);

        return Response
                .status(Response.Status.CREATED.getStatusCode())
                .entity(task)
                .build();
    }

    @DELETE
    @Transactional
    @Path("{id}")
    public Response deleteTask(@PathParam("id") Long id){
        Task task = taskRepository.findById(id);

        if(task != null){
            taskRepository.delete(task);
            return Response.noContent().build();
        }

        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @PUT
    @Transactional
    @Path("{id}")
    public Response updateTask(@PathParam("id") Long id, CreateTaskRequest taskData){
        Task task = taskRepository.findById(id);

        if(task != null){
            task.setText(taskData.getText());
            task.setDateTime(LocalDateTime.now());

            return Response.noContent().build();
        }

        return Response.status(Response.Status.NOT_FOUND).build();
    }



}


