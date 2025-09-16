package hariharan.tech.expensetracker.repos;

import hariharan.tech.expensetracker.models.Income;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IncomeRepo extends JpaRepository<Income, Integer> {
    List<Income> findByUserEmail(String userEmail);
}
