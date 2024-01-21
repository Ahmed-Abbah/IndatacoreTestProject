package abbah.ahmed.indatacoretestproject.entities;

import com.opencsv.bean.CsvBindByName;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployeeCsvFormat {
    @CsvBindByName(column = "firstname")
    private String fname;

    @CsvBindByName(column = "lastname")
    private String lname;

    @CsvBindByName(column = "email")
    private String email;

    @CsvBindByName(column = "joinedYear")
    private int joinedYear;

    @CsvBindByName(column = "salary")
    private String salary;

    @CsvBindByName(column = "gender")
    private String gender;


    @CsvBindByName(column = "department")
    private String depCode;
}
