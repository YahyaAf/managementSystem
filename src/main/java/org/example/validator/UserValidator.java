package org.example.validator;

import org.example.dto.UserRequestDTO;
import org.example.model.enums.Role;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class UserValidator {

    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    private static final int NOM_MIN_LENGTH = 3;
    private static final int NOM_MAX_LENGTH = 100;
    private static final int PASSWORD_MIN_LENGTH = 6;


    public static List<String> validateForCreation(UserRequestDTO dto) {
        List<String> errors = new ArrayList<>();

        if (dto == null) {
            errors.add("Request body cannot be null");
            return errors;
        }
        errors.addAll(validateNom(dto.getNom()));
        errors.addAll(validateEmail(dto.getEmail()));
        errors.addAll(validateMotDePasse(dto.getMotDePasse(), true));

        errors.addAll(validateRole(dto.getRole()));

        return errors;
    }


    public static List<String> validateForUpdate(UserRequestDTO dto) {
        List<String> errors = new ArrayList<>();

        if (dto == null) {
            errors.add("Request body cannot be null");
            return errors;
        }

        if (dto.getNom() != null) {
            errors.addAll(validateNom(dto.getNom()));
        }

        if (dto.getEmail() != null) {
            errors.addAll(validateEmail(dto.getEmail()));
        }

        if (dto.getMotDePasse() != null && !dto.getMotDePasse().isEmpty()) {
            errors.addAll(validateMotDePasse(dto.getMotDePasse(), false));
        }

        if (dto.getRole() != null) {
            errors.addAll(validateRole(dto.getRole()));
        }

        return errors;
    }


    private static List<String> validateNom(String nom) {
        List<String> errors = new ArrayList<>();

        if (nom == null || nom.trim().isEmpty()) {
            errors.add("Name is required");
            return errors;
        }

        String trimmedNom = nom.trim();

        if (trimmedNom.length() < NOM_MIN_LENGTH) {
            errors.add("Name must be at least " + NOM_MIN_LENGTH + " characters long");
        }

        if (trimmedNom.length() > NOM_MAX_LENGTH) {
            errors.add("Name must not exceed " + NOM_MAX_LENGTH + " characters");
        }

        return errors;
    }

    private static List<String> validateEmail(String email) {
        List<String> errors = new ArrayList<>();

        if (email == null || email.trim().isEmpty()) {
            errors.add("Email is required");
            return errors;
        }

        String trimmedEmail = email.trim();

        if (!EMAIL_PATTERN.matcher(trimmedEmail).matches()) {
            errors.add("Email format is invalid");
        }

        return errors;
    }

    private static List<String> validateMotDePasse(String motDePasse, boolean required) {
        List<String> errors = new ArrayList<>();

        if (required && (motDePasse == null || motDePasse.isEmpty())) {
            errors.add("Password is required");
            return errors;
        }

        if (motDePasse != null && !motDePasse.isEmpty()) {
            if (motDePasse.length() < PASSWORD_MIN_LENGTH) {
                errors.add("Password must be at least " + PASSWORD_MIN_LENGTH + " characters long");
            }
        }

        return errors;
    }

    private static List<String> validateRole(Role role) {
        List<String> errors = new ArrayList<>();

        if (role == null) {
            errors.add("Role is required");
            return errors;
        }

        try {
            Role.valueOf(role.name());
        } catch (IllegalArgumentException e) {
            errors.add("Role must be one of: ADMIN, USER, MANAGER, GUEST");
        }

        return errors;
    }

    public static List<String> validateId(Long id) {
        List<String> errors = new ArrayList<>();

        if (id == null) {
            errors.add("ID is required");
        } else if (id <= 0) {
            errors.add("ID must be a positive number");
        }

        return errors;
    }

    public static boolean isValid(List<String> errors) {
        return errors == null || errors.isEmpty();
    }

    public static String formatErrors(List<String> errors) {
        if (errors == null || errors.isEmpty()) {
            return "";
        }
        return String.join(", ", errors);
    }
}