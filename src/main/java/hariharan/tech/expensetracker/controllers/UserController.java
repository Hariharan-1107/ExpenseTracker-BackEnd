package hariharan.tech.expensetracker.controllers;


import hariharan.tech.expensetracker.models.*;
import hariharan.tech.expensetracker.repos.UserRepo;
import hariharan.tech.expensetracker.services.UserService;
import jakarta.transaction.Transactional;
import org.hibernate.Hibernate;
import org.hibernate.dialect.SybaseDialect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserService service;

    @GetMapping("/user")
    public ResponseEntity<?> getCurrentUser(Authentication authentication) {
        try {
            if (authentication == null || authentication.getPrincipal() == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "User not authenticated"));
            }

            User user = (User) authentication.getPrincipal();

            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Authentication failed", "message", e.getMessage()));
        }
    }

    @PostMapping("/category")
    public UserDTO addCategory(@RequestBody Category category, @AuthenticationPrincipal User principal) {
        User user = service.findByEmail(principal.getEmail());
        User updatedUser = service.addCategory(category, user);

        UserDTO userDTO = new UserDTO();
        userDTO.setEmail(updatedUser.getEmail());
        userDTO.setName(updatedUser.getName());
        userDTO.setPicture(updatedUser.getPicture());
        userDTO.setCategories(updatedUser.getCategories()); // Include if needed
        return userDTO;
    }

    @PutMapping("/category")
    public User updateCategory(@RequestBody Category category, @AuthenticationPrincipal User principal) {
        return service.updateCategory(category, principal);
    }

    @DeleteMapping("/category/{categoryId}")
    public ResponseEntity<?> deleteCategory(@PathVariable String categoryId, @AuthenticationPrincipal User authuser)
    {
        User user = service.deleteCategory(categoryId,authuser);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/budget")
    public UserDTO addBudget(@RequestBody Budget budget, @AuthenticationPrincipal User principal) {
        User user = service.findByEmail(principal.getEmail());
        User updatedUser = service.addBudget(budget,user);

        UserDTO userDTO = new UserDTO();
        userDTO.setEmail(updatedUser.getEmail());
        userDTO.setName(updatedUser.getName());
        userDTO.setPicture(updatedUser.getPicture());
        userDTO.setCategories(updatedUser.getCategories());
        userDTO.setBudgets(updatedUser.getBudgets());
        return userDTO;
    }

    @PutMapping("/budget")
    public User updateBudget(@RequestBody Budget budget, @AuthenticationPrincipal User principal) {
        return service.updateBudget(budget,principal);
    }

    @DeleteMapping("/budget/{categoryId}")
    public ResponseEntity<?> deleteBudget(@PathVariable String categoryId, @AuthenticationPrincipal User principal)
    {
        User user = service.deleteBudget(categoryId,principal);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @GetMapping("/categories")
    public List<Category> getAllCategories(@AuthenticationPrincipal User principal) {
        User user = service.findByEmail(principal.getEmail());
        return user.getCategories();
    }

    @GetMapping("/budgets")
    public List<Budget> getAllBudgets(@AuthenticationPrincipal User principal) {
        User user = service.findByEmail(principal.getEmail());
        return user.getBudgets();
    }
    @PutMapping("/budget/spent")
    public User updateBudgetSpent(@RequestBody BudgetRequest budgetRequest, @AuthenticationPrincipal User principal) {

        return service.updateBudgetSpent(budgetRequest,principal);
    }
}