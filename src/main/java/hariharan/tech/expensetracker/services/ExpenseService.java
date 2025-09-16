package hariharan.tech.expensetracker.services;

import hariharan.tech.expensetracker.models.Expense;
import hariharan.tech.expensetracker.models.User;
import hariharan.tech.expensetracker.repos.ExpenseRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExpenseService {

    @Autowired
    ExpenseRepo expenseRepo;

    public Expense addExpense (Expense expense, User user) {
        expense.setUser(user);
        return expenseRepo.save(expense);
    }

    public List<Expense> getAllExpenses(String email) {
        return expenseRepo.findByUserEmail(email);
    }
}
