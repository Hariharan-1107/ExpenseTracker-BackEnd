package hariharan.tech.expensetracker.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "incomes")
public class Income
{
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private int id;
    private long amount;
    private String source;
    private String description;
    private String date;
    private String type;
    @ManyToOne
    @JoinColumn(name = "user_email")
    private User user;
}
