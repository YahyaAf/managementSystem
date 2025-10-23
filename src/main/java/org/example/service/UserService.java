package org.example.service;

import org.example.dto.UserRequestDTO;
import org.example.dto.UserResponseDTO;
import org.example.model.User;
import org.example.mapper.UserMapper;
import org.example.model.enums.Role;
import org.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public UserResponseDTO createUser(UserRequestDTO requestDTO) {
        // ⭐ Validation du mot de passe
        if (requestDTO.getMotDePasse() == null || requestDTO.getMotDePasse().trim().isEmpty()) {
            throw new IllegalArgumentException("Password is required");
        }

        if (requestDTO.getMotDePasse().length() < 6) {
            throw new IllegalArgumentException("Password must be at least 6 characters");
        }

        if (userRepository.existsByEmail(requestDTO.getEmail())) {
            throw new RuntimeException("Email already exists: " + requestDTO.getEmail());
        }

        User user = UserMapper.toEntity(requestDTO);

        user.setMotDePasse(passwordEncoder.encode(requestDTO.getMotDePasse()));

        User savedUser = userRepository.save(user);

        return UserMapper.toResponse(savedUser);
    }

    @Transactional(readOnly = true)
    public List<UserResponseDTO> getAllUsers() {
        List<User> users = userRepository.findAllActive();
        return UserMapper.toResponseList(users);
    }

    @Transactional(readOnly = true)
    public Page<UserResponseDTO> getAllUsersPaginated(Pageable pageable) {
        Page<User> users = userRepository.findAll(pageable);
        return users.map(UserMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public UserResponseDTO getUserById(Long id) {
        User user = userRepository.findActiveById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        return UserMapper.toResponse(user);
    }

    @Transactional(readOnly = true)
    public UserResponseDTO getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));

        // ⭐ Vérifiez si l'utilisateur est supprimé
        if (user.getDeletedAt() != null) {
            throw new RuntimeException("User has been deleted");
        }

        return UserMapper.toResponse(user);
    }

    @Transactional(readOnly = true)
    public List<UserResponseDTO> searchUsersByName(String name) {
        List<User> users = userRepository.findByNomContainingIgnoreCase(name);
        return UserMapper.toResponseList(users);
    }

    @Transactional(readOnly = true)
    public List<UserResponseDTO> getUsersByRole(String role) {
        try {
            List<User> users = userRepository.findByRole(
                    Role.valueOf(role.toUpperCase())
            );
            return UserMapper.toResponseList(users);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid role: " + role + ". Valid roles are: " +
                    String.join(", ", getAllRoleNames()));
        }
    }

    @Transactional(readOnly = true)
    public List<UserResponseDTO> getActiveUsers() {
        List<User> users = userRepository.findByActif(true);
        return UserMapper.toResponseList(users);
    }

    public UserResponseDTO updateUser(Long id, UserRequestDTO requestDTO) {
        User existingUser = userRepository.findActiveById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        if (requestDTO.getEmail() != null &&
                !requestDTO.getEmail().equals(existingUser.getEmail()) &&
                userRepository.existsByEmail(requestDTO.getEmail())) {
            throw new RuntimeException("Email already exists: " + requestDTO.getEmail());
        }

        UserMapper.updateEntityFromDTO(existingUser, requestDTO);

        if (requestDTO.getMotDePasse() != null && !requestDTO.getMotDePasse().trim().isEmpty()) {
            if (requestDTO.getMotDePasse().length() < 6) {
                throw new IllegalArgumentException("Password must be at least 6 characters");
            }
            existingUser.setMotDePasse(passwordEncoder.encode(requestDTO.getMotDePasse()));
        }

        User updatedUser = userRepository.save(existingUser);

        return UserMapper.toResponse(updatedUser);
    }

    public UserResponseDTO toggleUserStatus(Long id) {
        User user = userRepository.findActiveById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        user.setActif(!user.getActif());
        User updatedUser = userRepository.save(user);

        return UserMapper.toResponse(updatedUser);
    }

    public void softDeleteUser(Long id) {
        User user = userRepository.findActiveById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        user.setDeletedAt(LocalDateTime.now());
        user.setActif(false);
        userRepository.save(user);
    }

    public void hardDeleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public long countActiveUsers() {
        return userRepository.countActiveUsers();
    }

    @Transactional(readOnly = true)
    public long countAllUsers() {
        return userRepository.count();
    }

    private String[] getAllRoleNames() {
        Role[] roles = Role.values();
        String[] roleNames = new String[roles.length];
        for (int i = 0; i < roles.length; i++) {
            roleNames[i] = roles[i].name();
        }
        return roleNames;
    }
}