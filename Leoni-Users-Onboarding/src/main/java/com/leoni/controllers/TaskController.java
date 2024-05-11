package com.leoni.controllers;

import com.leoni.dto.TaskDTO;
import com.leoni.models.Design;
import com.leoni.models.Task;
import com.leoni.models.User;
import com.leoni.repository.TaskRepository;
import com.leoni.security.services.DesignService;
import com.leoni.security.services.UserDetailsImpl;
import com.leoni.security.services.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserDetailsServiceImpl userService;

    @Autowired
    private DesignService designService;

    @GetMapping
    public ResponseEntity<List<TaskDTO>> getAllTasks() {
        List<Task> tasks = taskRepository.findAll();
        List<TaskDTO> taskDTOs = tasks.stream().map(this::convertToDTO).collect(Collectors.toList());
        return ResponseEntity.ok(taskDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskDTO> getTaskById(@PathVariable Long id) {
        Optional<Task> taskOptional = taskRepository.findById(id);
        return taskOptional.map(task -> ResponseEntity.ok().body(convertToDTO(task)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<TaskDTO> createTask(@RequestBody TaskDTO taskDTO) {
        Task createdTask = taskRepository.save(convertToEntity(taskDTO));
        TaskDTO createdTaskDTO = convertToDTO(createdTask);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTaskDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable Long id, @RequestBody TaskDTO updatedTaskDTO) {
        Optional<Task> existingTaskOptional = taskRepository.findById(id);
        if (existingTaskOptional.isPresent()) {
            Task existingTask = existingTaskOptional.get();
            Task updatedTaskEntity = updateTaskFromDTO(existingTask, updatedTaskDTO);
            taskRepository.save(updatedTaskEntity);
            return ResponseEntity.ok(updatedTaskEntity);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        Optional<Task> taskOptional = taskRepository.findById(id);
        if (taskOptional.isPresent()) {
            taskRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    private TaskDTO convertToDTO(Task task) {
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setUsername(task.getUser().getUsername());
        taskDTO.setDesignId(task.getDesign().getId());
        taskDTO.setState(task.getState());
        taskDTO.setPriority(task.getPriority());
        return taskDTO;
    }

    private Task convertToEntity(TaskDTO taskDTO) {
        Task task = new Task();
        User user = getUserFromDetailsService(taskDTO.getUsername());
        Design design = designService.getDesignById(taskDTO.getDesignId()).orElse(null);
        task.setUser(user);
        task.setDesign(design);
        task.setState(taskDTO.getState());
        task.setPriority(taskDTO.getPriority());
        return task;
    }

    private Task updateTaskFromDTO(Task task, TaskDTO taskDTO) {
        User user = getUserFromDetailsService(taskDTO.getUsername());
        Design design = designService.getDesignById(taskDTO.getDesignId()).orElse(null);
        task.setUser(user);
        task.setDesign(design);
        task.setState(taskDTO.getState());
        task.setPriority(taskDTO.getPriority());
        return task;
    }

    private User getUserFromDetailsService(String username) {
        UserDetails userDetails = userService.loadUserByUsername(username);
        if (userDetails instanceof UserDetailsImpl) {
            return ((UserDetailsImpl) userDetails).getUser();
        } else {
            throw new RuntimeException("User not found for username: " + username);
        }
    }
}
