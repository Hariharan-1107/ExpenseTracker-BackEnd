package hariharan.tech.expensetracker.models;

import lombok.Data;

import java.util.List;

@Data
public class UserDTO {
    private String email;
    private String name;
    private String picture;
    private List<Category> categories;
    private List<Budget> budgets;

}
