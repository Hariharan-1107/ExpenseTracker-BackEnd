package hariharan.tech.expensetracker.controllers;

import hariharan.tech.expensetracker.models.Expense;
import hariharan.tech.expensetracker.models.User;
import hariharan.tech.expensetracker.repos.ExpenseRepo;
import hariharan.tech.expensetracker.services.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ExpenseController {

    @Autowired
    ExpenseService expenseService;


    @GetMapping("/expenses")
    public List<Expense> getAllExpenses(@AuthenticationPrincipal User user){
        return  expenseService.getAllExpenses(user.getEmail());
    }
    @PostMapping("/expense")
    public Expense addExpense(@RequestBody Expense expense,@AuthenticationPrincipal User user) {
        return expenseService.addExpense(expense,user);
    }
}
