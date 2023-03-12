package com.gustavo.samplekubernetes.services;

import com.gustavo.samplekubernetes.dto.UserDTO;
import com.gustavo.samplekubernetes.dto.UserResponseDTO;
import com.gustavo.samplekubernetes.entities.User;
import com.gustavo.samplekubernetes.exceptions.ResourceNotFoundException;
import com.gustavo.samplekubernetes.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTests {
    @InjectMocks
    private UserService userService;
    @Mock
    private UserRepository userRepository;
    private Integer existingUserId;
    private Integer nonExistingUserId;
    private User existingUser;
    private User nonExistingUser;
    private UserDTO validUserDTO;

    @BeforeEach
    void setUp() throws Exception {

        existingUserId = 1;
        nonExistingUserId = 2;

        validUserDTO = new UserDTO("gustavo@gmail.com", "Gustavo", "Silva", "12345678");
        existingUser = new User(1, "Gustavo", "Silva", "gustavo.silva@email.com", "12345678");
        nonExistingUser = new User(null, "Gustavo", "Silva", "gustavo@gmail.com", "12345678");
    }

    @Test
    @DisplayName("It should get an user by ID when existing ID is informed")
    public void itShouldGetAnUserByIdWhenExistingUserIdIsInformed() {
        when(userRepository.findById(existingUserId)).thenReturn(Optional.of(existingUser));

        UserResponseDTO user = userService.getById(existingUserId);
        assertEquals(user.id(), existingUserId);
        assertEquals(user.email(), existingUser.getEmail());
        assertEquals(user.firstName(), existingUser.getFirstName());
        assertEquals(user.lastName(), existingUser.getLastName());
        verify(userRepository, times(1)).findById(existingUserId);
    }

    @Test
    @DisplayName("It should not be get an user by ID when non existing ID is informed")
    public void itShouldThrowsResourceNotFoundExceptionWhenNonExistingIdIsInformed() {
        when(userRepository.findById(nonExistingUserId)).thenReturn(Optional.empty());
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> userService.getById(nonExistingUserId));
        assertEquals(exception.getMessage(), "User Not Found: 2");
        verify(userRepository, times(1)).findById(nonExistingUserId);
    }

    @Test
    @DisplayName("It should return all users in the application")
    public void itShouldReturnAllUsers() {

        when(userRepository.findAll()).thenReturn(List.of(existingUser));

        List<UserResponseDTO> users = userService.getAll();

        assertEquals(users.stream().count(), 1L);
        verify(userRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("It should create a new user when valid data is informed")
    public void itShouldCreateANewUserWhenValidDTOIsInformed() {
        when(userRepository.save(nonExistingUser)).thenReturn(existingUser);

        UserResponseDTO user = userService.create(validUserDTO);

        assertEquals(user.id(), existingUser.getId());
        assertEquals(user.firstName(), existingUser.getFirstName());
        assertEquals(user.lastName(), existingUser.getLastName());
        assertEquals(user.email(), existingUser.getEmail());
        verify(userRepository, times(1)).save(nonExistingUser);
    }


}
