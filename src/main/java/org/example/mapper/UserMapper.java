package org.example.mapper;

import org.example.dto.UserRequestDTO;
import org.example.dto.UserResponseDTO;
import org.example.entity.User;

import java.util.List;
import java.util.stream.Collectors;

public class UserMapper {

    public static User toEntity(UserRequestDTO dto) {
        if (dto == null) {
            return null;
        }

        User user = new User();
        user.setNom(dto.getNom());
        user.setEmail(dto.getEmail());
        user.setMotDePasse(dto.getMotDePasse());
        user.setRole(dto.getRole());
        user.setActif(dto.getActif() != null ? dto.getActif() : true);

        return user;
    }

    public static UserResponseDTO toResponse(User user) {
        if (user == null) {
            return null;
        }

        UserResponseDTO response = new UserResponseDTO();
        response.setId(user.getId());
        response.setNom(user.getNom());
        response.setEmail(user.getEmail());
        response.setRole(user.getRole());
        response.setActif(user.getActif());
        response.setCreatedAt(user.getCreatedAt());
        response.setUpdatedAt(user.getUpdatedAt());

        return response;
    }

    public static void updateEntityFromDTO(User user, UserRequestDTO dto) {
        if (user == null || dto == null) {
            return;
        }

        if (dto.getNom() != null) {
            user.setNom(dto.getNom());
        }
        if (dto.getEmail() != null) {
            user.setEmail(dto.getEmail());
        }
        if (dto.getMotDePasse() != null && !dto.getMotDePasse().isEmpty()) {
            user.setMotDePasse(dto.getMotDePasse());
        }
        if (dto.getRole() != null) {
            user.setRole(dto.getRole());
        }
        if (dto.getActif() != null) {
            user.setActif(dto.getActif());
        }
    }

    public static List<UserResponseDTO> toResponseList(List<User> users) {
        if (users == null) {
            return null;
        }

        return users.stream()
                .map(UserMapper::toResponse)
                .collect(Collectors.toList());
    }
}