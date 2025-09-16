package hariharan.tech.expensetracker.services;

import hariharan.tech.expensetracker.models.Income;
import hariharan.tech.expensetracker.models.User;
import hariharan.tech.expensetracker.repos.IncomeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IncomeService {

    @Autowired
    IncomeRepo incomeRepo;

    public Income addIncome(Income income, User user) {
            income.setUser(user);
            return incomeRepo.save(income);
    }

    public List<Income> getAllIncomes(String email) {
        return  incomeRepo.findByUserEmail(email);
    }
}
