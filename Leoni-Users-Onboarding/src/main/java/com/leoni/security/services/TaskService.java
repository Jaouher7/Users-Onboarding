package com.leoni.security.services;

import com.leoni.models.Task;
import com.leoni.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

        private final TaskRepository taskRepository;

        @Autowired
        public TaskService(TaskRepository taskRepository) {
            this.taskRepository = taskRepository;
        }

        public List<Task> getAllTasks() {
            return taskRepository.findAll();
        }

        public Task getTaskById(Long id) {
            Optional<Task> optionalTask = taskRepository.findById(id);
            return optionalTask.orElse(null);
        }

        public Task createTask(Task task) {
            return taskRepository.save(task);
        }

        public Task updateTask(Long id, Task task) {
            if (taskRepository.existsById(id)) {
                task.setId(id);
                return taskRepository.save(task);
            } else {
                return null; // or throw an exception
            }
        }

        public void deleteTask(Long id) {
            taskRepository.deleteById(id);
        }
    }

