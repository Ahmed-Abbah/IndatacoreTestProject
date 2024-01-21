package abbah.ahmed.indatacoretestproject.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="employee")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Employee{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(name="first_name")
    private String firstName;

    @Column(name="last_name")
    private String lastName;

    @Column(name="gender")
    private String Gender;

    @Column(name="email")
    private String email;

    @Column(name="salary")
    private String salary;


    @Column(name = "joined_year")
    private int joinedYear;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.EAGER,cascade = CascadeType.MERGE)
    @JoinColumn(name="departement_id")
    private Departement departement;

    @Transient
    public String getDepartementName() {
        return departement != null ? departement.getName() : null;
    }


}
