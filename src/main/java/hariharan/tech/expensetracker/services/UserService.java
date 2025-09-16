package hariharan.tech.expensetracker.services;

import hariharan.tech.expensetracker.models.Budget;
import hariharan.tech.expensetracker.models.BudgetRequest;
import hariharan.tech.expensetracker.models.Category;
import hariharan.tech.expensetracker.models.User;
import hariharan.tech.expensetracker.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    @Autowired
    UserRepo repo;


    public User addCategory(Category category, User user) {
        category.setId(category.getName().toLowerCase());
        if (user.getCategories() == null) {
            user.setCategories(new ArrayList<>(List.of(category)));
        } else {
            user.getCategories().add(category);
        }
        return repo.save(user);
    }

    public User findByEmail(String email) {
        return repo.findByEmail(email).orElse(null);
    }

    public User addBudget(Budget budget, User user) {
        if(user.getBudgets() == null) {
            user.setBudgets(new ArrayList<>(List.of(budget)));
        }else {
            user.getBudgets().add(budget);
        }
        return repo.save(user);
    }



    public User updateBudgetSpent(BudgetRequest budgetRequest, User principal) {

        User user = repo.findByEmail(principal.getEmail()).orElse(null);
        if(user!=null) {
            List<Budget> budgets = user.getBudgets();
            System.out.println("Budgets: " + budgets);
            for (Budget b : budgets) {
                if (b.getCategoryId().equals(budgetRequest.getCategory())) {
                    System.out.println("Updating Budget: " + b);
                    b.setSpent(b.getSpent() + budgetRequest.getAmount());
                }
            }
            user.setBudgets(budgets);
            System.out.println("Updated Budgets: " + user.getBudgets());
        }

        return repo.save(user);
    }

    public User updateBudget(Budget budget, User principal) {
        User user = repo.findByEmail(principal.getEmail()).orElse(null);
        if(user!=null) {
            List<Budget> budgets = user.getBudgets();
            System.out.println("Budgets: " + budgets);
            for (Budget b : budgets) {
                if (b.getCategoryId().equals(budget.getCategoryId())) {
                    System.out.println("Updating Budget: " + b);
                    b.setAmount(budget.getAmount());
                }
            }
            user.setBudgets(budgets);
        }
        return repo.save(user);
    }

    public User updateCategory(Category category, User principal) {

        User user = repo.findByEmail(principal.getEmail()).orElse(null);
        if(user!=null){
            List<Category> categories = user.getCategories();
            for(Category c  : categories)
            {
                if(c.getId().equals(category.getId()))
                {
                    c.setId(category.getName().toLowerCase());
                    c.setName(category.getName());
                    c.setColor(category.getColor());
                    c.setIcon(category.getIcon());
                    break;
                }
            }
            user.setCategories(categories);
        }
        return repo.save(user);
    }

    public User deleteCategory(String categoryId, User authuser) {
        User user = repo.findByEmail(authuser.getEmail()).orElse(null);
        if(user!=null) {
            List<Category> categories = user.getCategories();
            for (Category c : categories) {
                if (c.getId().equals(categoryId)) {
                    categories.remove(c);
                    break;
                }
            }
            user.setCategories(categories);
        }
        return repo.save(user);
    }

    public User deleteBudget(String categoryId, User principal) {

        User user = repo.findByEmail(principal.getEmail()).orElse(null);
        if(user!=null) {
            List<Budget> budgets = user.getBudgets();
            for (Budget b : budgets) {
                if (b.getCategoryId().equals(categoryId)) {
                    budgets.remove(b);
                    break;
                }
            }
            user.setBudgets(budgets);
        }
        return repo.save(user);
    }
}
