package com.suhana.taskmanager.service;

import com.suhana.taskmanager.dto.TaskRequest;
import com.suhana.taskmanager.exception.TaskNotFoundException;
import com.suhana.taskmanager.model.Task;
import com.suhana.taskmanager.model.TaskStatus;
import com.suhana.taskmanager.repository.TaskRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class TaskService {
    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public Task create(TaskRequest request) {
        Task task = new Task(request.getTitle().trim(), request.getDescription(),
                request.getStatus() == null ? TaskStatus.PENDING : request.getStatus());
        return taskRepository.save(task);
    }

    @Transactional(readOnly = true)
    public List<Task> getAll() {
        return taskRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Task getById(Long id) {
        return taskRepository.findById(id).orElseThrow(() -> new TaskNotFoundException(id));
    }

    public Task update(Long id, TaskRequest request) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new TaskNotFoundException(id));
        task.setTitle(request.getTitle().trim());
        task.setDescription(request.getDescription());
        task.setStatus(request.getStatus() == null ? task.getStatus() : request.getStatus());
        return taskRepository.save(task);
    }

    public void delete(Long id) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new TaskNotFoundException(id));
        taskRepository.delete(task);
    }
}
