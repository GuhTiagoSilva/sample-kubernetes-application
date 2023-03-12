package com.gustavo.samplekubernetes.controllers;

import com.gustavo.samplekubernetes.dto.UserDTO;
import com.gustavo.samplekubernetes.dto.UserResponseDTO;
import com.gustavo.samplekubernetes.services.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserResponseDTO getById(@PathVariable Integer id) {
        log.info("Searching for user with ID: {}", id);
        return userService.getById(id);
    }
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<UserResponseDTO> getAll() {
        log.info("Searching for user with ID");
        return userService.getAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseDTO create(@Valid @RequestBody UserDTO userDTO) {
        log.info("Creating a new user with payload: {}", userDTO);
        return userService.create(userDTO);
    }
}
