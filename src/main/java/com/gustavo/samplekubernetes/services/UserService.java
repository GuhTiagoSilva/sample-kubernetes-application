package com.gustavo.samplekubernetes.services;

import com.gustavo.samplekubernetes.dto.UserDTO;
import com.gustavo.samplekubernetes.dto.UserResponseDTO;
import com.gustavo.samplekubernetes.entities.User;
import com.gustavo.samplekubernetes.exceptions.ResourceNotFoundException;
import com.gustavo.samplekubernetes.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public UserResponseDTO getById(Integer id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    log.info("User with ID: {} not found.", id);
                    return new ResourceNotFoundException("User Not Found: " + id);
                });

        UserResponseDTO userResponseDTO = new UserResponseDTO(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail());
        return userResponseDTO;
    }

    @Transactional(readOnly = true)
    public List<UserResponseDTO> getAll() {
        return userRepository
                .findAll()
                .stream()
                .map(user -> new UserResponseDTO(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail()))
                .collect(Collectors.toList());
    }

    @Transactional
    public UserResponseDTO create(UserDTO userDTO) {
        User user = new User();
        user.setEmail(userDTO.email());
        user.setPassword(userDTO.password());
        user.setFirstName(userDTO.firstName());
        user.setLastName(userDTO.lastName());
        user = userRepository.save(user);
        log.info("User created successfully");
        return new UserResponseDTO(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail());
    }

}
