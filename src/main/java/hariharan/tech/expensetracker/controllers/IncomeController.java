package hariharan.tech.expensetracker.controllers;

import hariharan.tech.expensetracker.models.Expense;
import hariharan.tech.expensetracker.models.Income;
import hariharan.tech.expensetracker.models.User;
import hariharan.tech.expensetracker.services.IncomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class IncomeController {

    @Autowired
    IncomeService service;

    @GetMapping("/incomes")
    public List<Income> getAllIncomes(@AuthenticationPrincipal User user){
        return  service.getAllIncomes(user.getEmail());
    }
    @PostMapping("/income")
    public Income addIncome(@RequestBody Income income, @AuthenticationPrincipal User user) {
        return service.addIncome(income,user);
    }
}
