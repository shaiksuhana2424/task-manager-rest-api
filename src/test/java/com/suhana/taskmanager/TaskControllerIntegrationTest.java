package com.suhana.taskmanager;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.suhana.taskmanager.dto.TaskRequest;
import com.suhana.taskmanager.model.TaskStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class TaskControllerIntegrationTest {
    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    @Test
    void createAndFetchTask() throws Exception {
        TaskRequest request = new TaskRequest();
        request.setTitle("Prepare assignment");
        request.setDescription("Implement CRUD API");
        request.setStatus(TaskStatus.PENDING);

        String response = mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.title", is("Prepare assignment")))
                .andReturn().getResponse().getContentAsString();

        Long id = objectMapper.readTree(response).get("id").asLong();
        mockMvc.perform(get("/api/tasks/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Prepare assignment")));
    }

    @Test
    void blankTitleShouldReturnValidationError() throws Exception {
        TaskRequest request = new TaskRequest();
        request.setTitle("  ");

        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is("Validation failed")))
                .andExpect(jsonPath("$.validationErrors.title", not(emptyOrNullString())));
    }

    @Test
    void missingTaskShouldReturn404() throws Exception {
        mockMvc.perform(get("/api/tasks/987654"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", is(404)));
    }

    @Test
    void deleteShouldReturn204() throws Exception {
        TaskRequest request = new TaskRequest();
        request.setTitle("Delete me");
        String response = mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        Long id = objectMapper.readTree(response).get("id").asLong();

        mockMvc.perform(delete("/api/tasks/" + id))
                .andExpect(status().isNoContent());
        mockMvc.perform(get("/api/tasks/" + id))
                .andExpect(status().isNotFound());
    }
}
