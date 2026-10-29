package com.suhana.taskmanager.service;

import com.suhana.taskmanager.dto.TaskRequest;
import com.suhana.taskmanager.exception.TaskNotFoundException;
import com.suhana.taskmanager.model.Task;
import com.suhana.taskmanager.model.TaskStatus;
import com.suhana.taskmanager.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {
    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    @Test
    void createShouldTrimTitleAndDefaultStatus() {
        TaskRequest request = new TaskRequest();
        request.setTitle("  Write tests  ");
        request.setDescription("Add unit tests");
        when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Task result = taskService.create(request);

        assertEquals("Write tests", result.getTitle());
        assertEquals(TaskStatus.PENDING, result.getStatus());
        verify(taskRepository).save(any(Task.class));
    }

    @Test
    void getByIdShouldThrowWhenTaskDoesNotExist() {
        when(taskRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class, () -> taskService.getById(99L));
    }

    @Test
    void deleteShouldDeleteExistingTask() {
        Task task = new Task("Sample", "Description", TaskStatus.PENDING);
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        taskService.delete(1L);

        verify(taskRepository).delete(task);
    }
}
