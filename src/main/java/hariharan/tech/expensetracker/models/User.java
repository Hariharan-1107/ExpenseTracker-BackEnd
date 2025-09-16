package hariharan.tech.expensetracker.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "users")
public class User {

    @Id
    @Column(name = "user_email", unique = true)
    private String email;

    private String name;
    private String picture;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Expense> expenses;

    @ElementCollection
    @JsonIgnore
    @CollectionTable(
            name = "user_categories",
            joinColumns = @JoinColumn(name = "user_email") // 👈 now FK is email
    )
    private List<Category> categories;


    @ElementCollection
    @JsonIgnore
    @CollectionTable(
            name = "user_budgets",
            joinColumns = @JoinColumn(name = "user_email") // 👈 now FK is email
    )
    private List<Budget> budgets;


    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
