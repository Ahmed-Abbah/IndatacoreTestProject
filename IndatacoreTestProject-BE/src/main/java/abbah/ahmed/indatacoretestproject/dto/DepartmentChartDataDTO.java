package abbah.ahmed.indatacoretestproject.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DepartmentChartDataDTO {
    private String departmentName;
    private int numberOfEmployees;
}