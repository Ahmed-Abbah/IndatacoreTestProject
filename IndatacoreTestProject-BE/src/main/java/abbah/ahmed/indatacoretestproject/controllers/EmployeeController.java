package abbah.ahmed.indatacoretestproject.controllers;


import abbah.ahmed.indatacoretestproject.Service.EmployeeService;

import abbah.ahmed.indatacoretestproject.entities.Employee;
import abbah.ahmed.indatacoretestproject.repositories.DepartementRepository;
import abbah.ahmed.indatacoretestproject.repositories.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class EmployeeController {

    private EmployeeRepository employeeRepository;


    private DepartementRepository departementRepository;


    private EmployeeService service;

    @Autowired
    public EmployeeController(EmployeeRepository employeeRepository, DepartementRepository departementRepository, EmployeeService service) {
        this.employeeRepository = employeeRepository;
        this.departementRepository = departementRepository;
        this.service = service;
    }




    @GetMapping("/employees")
    public List<Employee> getEmployees() {
        return employeeRepository.findAll();
    }

    @PatchMapping("/employees/{id}")
    public ResponseEntity<Employee> updateEmployee(
            @PathVariable Long id,
            @RequestBody Employee employeeUpdates) {
        Optional<Employee> employeeOptional = employeeRepository.findById(id);
        if(employeeOptional.isPresent()){
            Employee employee = employeeOptional.get();
            employee.setFirstName(employeeUpdates.getFirstName());
            employee.setLastName(employeeUpdates.getLastName());
            employee.setJoinedYear(employeeUpdates.getJoinedYear());
            employee.setEmail(employeeUpdates.getEmail());
            employee.setDepartement(employeeUpdates.getDepartement());
            employeeRepository.save(employee);
            return new ResponseEntity<>(employee, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(employeeUpdates, HttpStatus.NOT_FOUND);
        }
    }
    @DeleteMapping("/employees/{id}")
    public void deleteEmployee(@PathVariable Long id) {
        employeeRepository.findById(id).get().setDepartement(null);
        employeeRepository.deleteById(id);
    }

    @PostMapping("/employees")
    public ResponseEntity<Employee> addEmployee(@RequestBody Employee employee) {
        String email = employee.getEmail();
        Optional<Employee> optionalEmployee = employeeRepository.findByEmail(email);
        if(optionalEmployee.isPresent()){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        employeeRepository.save(employee);
        return new ResponseEntity<>(employee, HttpStatus.CREATED);
    }
    @PostMapping(value = "/upload", consumes = {"multipart/form-data"})
    public ResponseEntity<Integer> uploadEmployees(
            @RequestPart("file") MultipartFile file
    ) throws IOException, InterruptedException {
        return ResponseEntity.ok(service.uploadEmployees(file));
    }
    @GetMapping("/employees/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable Long id) {
        Optional<Employee> employee = employeeRepository.findById(id);
        return employee.map(response -> ResponseEntity.ok().body(response))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }


}
