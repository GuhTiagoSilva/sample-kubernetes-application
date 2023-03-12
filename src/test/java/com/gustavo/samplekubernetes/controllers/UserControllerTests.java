package com.gustavo.samplekubernetes.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gustavo.samplekubernetes.dto.UserDTO;
import com.gustavo.samplekubernetes.dto.UserResponseDTO;
import com.gustavo.samplekubernetes.exceptions.ResourceNotFoundException;
import com.gustavo.samplekubernetes.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class UserControllerTests {
    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    @MockBean
    private UserService userService;
    private Integer existingId;
    private Integer nonExistingId;
    private UserResponseDTO validUserResponseDTO;
    private UserDTO validUserDTO;

    @Autowired
    public UserControllerTests(MockMvc mockMvc, ObjectMapper objectMapper) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
    }

    @BeforeEach
    void setUp() throws Exception {
        this.existingId = 1;
        this.nonExistingId = 2;

        this.validUserResponseDTO = new UserResponseDTO(1, "Gustavo", "Silva", "gustavo@gmail.com");
        this.validUserDTO = new UserDTO("gustavo@gmail.com", "Gustavo", "Silva", "12345678");
    }

    @Test
    @DisplayName("It should get an user by ID when existing ID is informed")
    public void itShouldGetUserByIdWhenExistingIdInformed() throws Exception {

        when(userService.getById(this.existingId)).thenReturn(validUserResponseDTO);

        ResultActions resultActions = this.mockMvc
                .perform(get("/users/{id}", existingId)
                        .contentType(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$.id").exists());
        resultActions.andExpect(jsonPath("$.email").exists());
        resultActions.andExpect(jsonPath("$.firstName").exists());
        resultActions.andExpect(jsonPath("$.lastName").exists());

        verify(userService, times(1)).getById(existingId);
    }

    @Test
    @DisplayName("It should return resource not found error when non existing ID is informed")
    public void itShouldReturnNotResourceNotFoundWhenNonExistingIdIsInformed() throws Exception {
        when(userService.getById(this.nonExistingId)).thenThrow(new ResourceNotFoundException("User Not Found: 2"));

        ResultActions resultActions = this.mockMvc
                .perform(get("/users/{id}", nonExistingId)
                        .contentType(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isNotFound());
        resultActions.andExpect(jsonPath("$.timestamp").exists());
        resultActions.andExpect(jsonPath("$.path").exists());
        resultActions.andExpect(jsonPath("$.error").exists());
        resultActions.andExpect(jsonPath("$.message").exists());

        verify(userService, times(1)).getById(nonExistingId);
    }

    @Test
    @DisplayName("It should create a new user when valid data is informed")
    public void itShouldCreateANewUserWhenValidPayload() throws Exception {
        when(userService.create(validUserDTO)).thenReturn(validUserResponseDTO);

        String jsonBody = objectMapper.writeValueAsString(validUserDTO);

        ResultActions result =
                mockMvc.perform(post("/users")
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isCreated());
        result.andExpect(jsonPath("$.id").exists());
        result.andExpect(jsonPath("$.firstName").exists());
        result.andExpect(jsonPath("$.lastName").exists());
        result.andExpect(jsonPath("$.email").exists());
    }


}
