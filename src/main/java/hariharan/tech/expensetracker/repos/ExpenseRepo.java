package hariharan.tech.expensetracker.repos;

import hariharan.tech.expensetracker.models.Expense;
import hariharan.tech.expensetracker.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExpenseRepo extends JpaRepository<Expense, Integer> {

    List<Expense> findByUserEmail(String email);
}
