package abbah.ahmed.indatacoretestproject.controllers;


import abbah.ahmed.indatacoretestproject.dto.DepartmentChartDataDTO;
import abbah.ahmed.indatacoretestproject.entities.Departement;
import abbah.ahmed.indatacoretestproject.repositories.DepartementRepository;
import abbah.ahmed.indatacoretestproject.repositories.EmployeeRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
public class DepartementController {
    private EmployeeRepository employeeRepository;
    private DepartementRepository departementRepository;
    @Autowired
    public DepartementController(EmployeeRepository employeeRepository, DepartementRepository departementRepository) {
        this.employeeRepository = employeeRepository;
        this.departementRepository = departementRepository;
    }

    @GetMapping("/departements")
    public List<Departement> getDepartements() {
        return departementRepository.findAll();
    }


    @PatchMapping("/departements/{id}")
    public ResponseEntity<Departement> updateDepartement(
            @PathVariable Long id,
            @RequestBody Departement departementUpdates) {
            if (departementUpdates.getName() == null) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            Departement departement = departementRepository.findById(id).get();
            departement.setName(departementUpdates.getName());
            departement.setCode(departementUpdates.getCode());
            departementRepository.save(departement);
        return new ResponseEntity<>(departement, HttpStatus.OK);
    }

    @DeleteMapping("/departements/{id}")
    public void deleteDepartement(@PathVariable Long id) {
        departementRepository.findById(id).get().getEmployees().forEach(employee -> {
            employee.setDepartement(null);
            employeeRepository.save(employee);
        });
        departementRepository.deleteById(id);
    }

    @PostMapping("/departements")
    public ResponseEntity<Departement> addDepartement(@RequestBody Departement departement) {
        String code = departement.getCode();

        Departement existingDepartement = departementRepository.findByCode(code);
        if (existingDepartement != null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Departement newDepatement = departement;
        departementRepository.save(newDepatement);


        return new ResponseEntity<>(newDepatement, HttpStatus.CREATED);
    }
    @GetMapping("/departements/{id}")
    public ResponseEntity<Departement> getDepartementById(@PathVariable Long id) {
        Optional<Departement> departement = departementRepository.findById(id);
        return departement.map(response -> ResponseEntity.ok().body(response))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    @GetMapping("/data")
    public List<DepartmentChartDataDTO> getDepartmentEmployeeCount(){
        List<Departement> departments = departementRepository.findAll();
        return departments.stream()
                .map(department -> {
                    DepartmentChartDataDTO chartData = new DepartmentChartDataDTO();
                    chartData.setDepartmentName(department.getName());
                    chartData.setNumberOfEmployees(department.getEmployees().size());
                    return chartData;
                })
                .collect(Collectors.toList());
    }
}
