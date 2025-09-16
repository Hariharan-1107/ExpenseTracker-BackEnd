package hariharan.tech.expensetracker.models;

import lombok.Data;

@Data
public class BudgetRequest {
    private Long amount;
    private String category;
}
