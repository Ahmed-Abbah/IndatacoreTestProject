package abbah.ahmed.indatacoretestproject.Service;


import abbah.ahmed.indatacoretestproject.entities.Employee;
import abbah.ahmed.indatacoretestproject.entities.EmployeeCsvFormat;

import abbah.ahmed.indatacoretestproject.repositories.DepartementRepository;
import abbah.ahmed.indatacoretestproject.repositories.EmployeeRepository;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeService {
    private DepartementRepository departementRepository;
    private EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeService(DepartementRepository departementRepository, EmployeeRepository employeeRepository) {
        this.departementRepository = departementRepository;
        this.employeeRepository = employeeRepository;
    }


    public Integer uploadEmployees(MultipartFile file) throws IOException {
        Set<Employee> employees = readCsvFile(file);
        employeeRepository.saveAll(employees);
        return employees.size();
    }

    private Set<Employee> readCsvFile(MultipartFile file) throws IOException {
        try(Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            HeaderColumnNameMappingStrategy<EmployeeCsvFormat> strategy =
                    new HeaderColumnNameMappingStrategy<>();
            strategy.setType(EmployeeCsvFormat.class);
            CsvToBean<EmployeeCsvFormat> csvToBean =
                    new CsvToBeanBuilder<EmployeeCsvFormat>(reader)
                            .withMappingStrategy(strategy)
                            .withIgnoreEmptyLine(true)
                            .withIgnoreLeadingWhiteSpace(true)
                            .build();
            return csvToBean.parse()
                    .stream()
                    .map(csvLine -> Employee.builder()
                            .firstName(csvLine.getFname())
                            .lastName(csvLine.getLname())
                            .joinedYear(csvLine.getJoinedYear())
                            .email(csvLine.getEmail())
                            .salary(csvLine.getSalary())
                            .Gender(csvLine.getGender())
                            .departement(departementRepository.findByCode(csvLine.getDepCode()))
                            .build()
                    )
                    .collect(Collectors.toSet());
        }
    }
}