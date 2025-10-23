package org.example.controller;

import org.example.dto.UserRequestDTO;
import org.example.dto.UserResponseDTO;
import org.example.repository.UserRepository;
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

    private UserService userService;

    @Autowired
    public UserController(UserService userService){
        this.userService = userService;
    }


    @PostMapping
    public ResponseEntity<Map<String, Object>> createUser(@RequestBody UserRequestDTO request) {
        Map<String, Object> response = new HashMap<>();
        try {
            UserResponseDTO user = userService.createUser(request);
            response.put("status", "success");
            response.put("message", "User created successfully");
            response.put("data", user);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }


    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllUsers() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<UserResponseDTO> users = userService.getAllUsers();
            response.put("status", "success");
            response.put("count", users.size());
            response.put("data", users);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/paginated")
    public ResponseEntity<Map<String, Object>> getAllUsersPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction
    ) {
        Map<String, Object> response = new HashMap<>();
        try {
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
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }


    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getUserById(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            UserResponseDTO user = userService.getUserById(id);
            response.put("status", "success");
            response.put("data", user);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }


    @GetMapping("/email/{email}")
    public ResponseEntity<Map<String, Object>> getUserByEmail(@PathVariable String email) {
        Map<String, Object> response = new HashMap<>();
        try {
            UserResponseDTO user = userService.getUserByEmail(email);
            response.put("status", "success");
            response.put("data", user);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }


    @GetMapping("/search")
    public ResponseEntity<Map<String, Object>> searchUsers(@RequestParam String name) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<UserResponseDTO> users = userService.searchUsersByName(name);
            response.put("status", "success");
            response.put("count", users.size());
            response.put("data", users);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }


    @GetMapping("/role/{role}")
    public ResponseEntity<Map<String, Object>> getUsersByRole(@PathVariable String role) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<UserResponseDTO> users = userService.getUsersByRole(role);
            response.put("status", "success");
            response.put("count", users.size());
            response.put("data", users);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateUser(
            @PathVariable Long id,
            @RequestBody UserRequestDTO request
    ) {
        Map<String, Object> response = new HashMap<>();
        try {
            UserResponseDTO user = userService.updateUser(id, request);
            response.put("status", "success");
            response.put("message", "User updated successfully");
            response.put("data", user);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }


    @DeleteMapping("/{id}/soft")
    public ResponseEntity<Map<String, Object>> softDeleteUser(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            userService.softDeleteUser(id);
            response.put("status", "success");
            response.put("message", "User soft deleted successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @DeleteMapping("/{id}/hard")
    public ResponseEntity<Map<String, Object>> hardDeleteUser(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            userService.hardDeleteUser(id);
            response.put("status", "success");
            response.put("message", "User permanently deleted");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

}