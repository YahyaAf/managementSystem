package org.example.controller;

import jakarta.servlet.http.HttpSession;
import org.example.dto.UserRequestDTO;
import org.example.dto.UserResponseDTO;
import org.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService){
        this.userService = userService;
    }

    private void checkAuthenticated(HttpSession session) {
        Long userId = (Long) session.getAttribute("currentUser");
        if (userId == null) {
            throw new RuntimeException("Authentication required. Please login.");
        }
    }

    private void checkAdmin(HttpSession session) {
        checkAuthenticated(session);
        String userRole = (String) session.getAttribute("userRole");
        if (!"ADMIN".equals(userRole)) {
            throw new RuntimeException("Access denied. Admin role required.");
        }
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> createUser(
            @RequestBody UserRequestDTO request,
            HttpSession session
    ) {
        Map<String, Object> response = new HashMap<>();
        try {
            checkAdmin(session);

            UserResponseDTO user = userService.createUser(request);
            response.put("status", "success");
            response.put("message", "User created successfully");
            response.put("data", user);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
            HttpStatus status = e.getMessage().contains("Authentication")
                    ? HttpStatus.UNAUTHORIZED
                    : e.getMessage().contains("Access denied")
                    ? HttpStatus.FORBIDDEN
                    : HttpStatus.BAD_REQUEST;
            return ResponseEntity.status(status).body(response);
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllUsers(HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        try {
            checkAuthenticated(session);

            List<UserResponseDTO> users = userService.getAllUsers();
            response.put("status", "success");
            response.put("count", users.size());
            response.put("data", users);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/paginated")
    public ResponseEntity<Map<String, Object>> getAllUsersPaginated(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sortBy", defaultValue = "id") String sortBy,
            @RequestParam(value = "direction", defaultValue = "asc") String direction,
            HttpSession session
    ) {
        Map<String, Object> response = new HashMap<>();
        try {
            checkAuthenticated(session);

            Sort.Direction sortDirection = direction.equalsIgnoreCase("desc")
                    ? Sort.Direction.DESC : Sort.Direction.ASC;
            Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));

            Page<UserResponseDTO> usersPage = userService.getAllUsersPaginated(pageable);

            response.put("status", "success");
            response.put("data", usersPage.getContent());
            response.put("currentPage", usersPage.getNumber());
            response.put("totalPages", usersPage.getTotalPages());
            response.put("totalElements", usersPage.getTotalElements());

            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getUserById(
            @PathVariable("id") Long id,
            HttpSession session
    ) {
        Map<String, Object> response = new HashMap<>();
        try {
            checkAuthenticated(session);

            UserResponseDTO user = userService.getUserById(id);
            response.put("status", "success");
            response.put("data", user);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
            HttpStatus status = e.getMessage().contains("Authentication")
                    ? HttpStatus.UNAUTHORIZED
                    : HttpStatus.NOT_FOUND;
            return ResponseEntity.status(status).body(response);
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<Map<String, Object>> getUserByEmail(
            @PathVariable("email") String email,
            HttpSession session
    ) {
        Map<String, Object> response = new HashMap<>();
        try {
            checkAuthenticated(session);

            UserResponseDTO user = userService.getUserByEmail(email);
            response.put("status", "success");
            response.put("data", user);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
            HttpStatus status = e.getMessage().contains("Authentication")
                    ? HttpStatus.UNAUTHORIZED
                    : HttpStatus.NOT_FOUND;
            return ResponseEntity.status(status).body(response);
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @GetMapping("/search")
    public ResponseEntity<Map<String, Object>> searchUsers(
            @RequestParam(value = "name") String name,
            HttpSession session
    ) {
        Map<String, Object> response = new HashMap<>();
        try {
            checkAuthenticated(session);

            List<UserResponseDTO> users = userService.searchUsersByName(name);
            response.put("status", "success");
            response.put("count", users.size());
            response.put("data", users);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/role/{role}")
    public ResponseEntity<Map<String, Object>> getUsersByRole(
            @PathVariable("role") String role,
            HttpSession session
    ) {
        Map<String, Object> response = new HashMap<>();
        try {
            checkAuthenticated(session);

            List<UserResponseDTO> users = userService.getUsersByRole(role);
            response.put("status", "success");
            response.put("count", users.size());
            response.put("data", users);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
            HttpStatus status = e.getMessage().contains("Authentication")
                    ? HttpStatus.UNAUTHORIZED
                    : HttpStatus.BAD_REQUEST;
            return ResponseEntity.status(status).body(response);
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateUser(
            @PathVariable("id") Long id,
            @RequestBody UserRequestDTO request,
            HttpSession session
    ) {
        Map<String, Object> response = new HashMap<>();
        try {
            checkAdmin(session);

            UserResponseDTO user = userService.updateUser(id, request);
            response.put("status", "success");
            response.put("message", "User updated successfully");
            response.put("data", user);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
            HttpStatus status = e.getMessage().contains("Authentication")
                    ? HttpStatus.UNAUTHORIZED
                    : e.getMessage().contains("Access denied")
                    ? HttpStatus.FORBIDDEN
                    : HttpStatus.BAD_REQUEST;
            return ResponseEntity.status(status).body(response);
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @DeleteMapping("/{id}/soft")
    public ResponseEntity<Map<String, Object>> softDeleteUser(
            @PathVariable("id") Long id,
            HttpSession session
    ) {
        Map<String, Object> response = new HashMap<>();
        try {
            checkAdmin(session);

            userService.softDeleteUser(id);
            response.put("status", "success");
            response.put("message", "User soft deleted successfully");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
            HttpStatus status = e.getMessage().contains("Authentication")
                    ? HttpStatus.UNAUTHORIZED
                    : e.getMessage().contains("Access denied")
                    ? HttpStatus.FORBIDDEN
                    : HttpStatus.NOT_FOUND;
            return ResponseEntity.status(status).body(response);
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @DeleteMapping("/{id}/hard")
    public ResponseEntity<Map<String, Object>> hardDeleteUser(
            @PathVariable("id") Long id,
            HttpSession session
    ) {
        Map<String, Object> response = new HashMap<>();
        try {
            checkAdmin(session);

            userService.hardDeleteUser(id);
            response.put("status", "success");
            response.put("message", "User permanently deleted");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
            HttpStatus status = e.getMessage().contains("Authentication")
                    ? HttpStatus.UNAUTHORIZED
                    : e.getMessage().contains("Access denied")
                    ? HttpStatus.FORBIDDEN
                    : HttpStatus.NOT_FOUND;
            return ResponseEntity.status(status).body(response);
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
}