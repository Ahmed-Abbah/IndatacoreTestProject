package abbah.ahmed.indatacoretestproject;

import abbah.ahmed.indatacoretestproject.entities.Departement;
import abbah.ahmed.indatacoretestproject.entities.Employee;
import abbah.ahmed.indatacoretestproject.entities.User;
import abbah.ahmed.indatacoretestproject.repositories.DepartementRepository;
import abbah.ahmed.indatacoretestproject.repositories.EmployeeRepository;

import abbah.ahmed.indatacoretestproject.repositories.UserIndatacoreRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SpringBootApplication
public class IndatacoreTestProjectApplication {
	@Autowired
	private UserIndatacoreRepo userIndatacore;

	@Autowired
	private DepartementRepository departementRepo;

	@Autowired
	private EmployeeRepository employeeRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(IndatacoreTestProjectApplication.class, args);
	}
	@Bean
	@Order(1)
	public CommandLineRunner initData() {
		return args -> {
			// Check if the database is empty
			if (departementRepo.count() == 0) {
				// Initialization code
				userIndatacore.save(User.builder()
						.email("abbah.ahmed@etu.uae.ac.ma")
						.firstName("Ahmed")
						.lastName("Abbah")
						.roles("USER")
						.password(passwordEncoder.encode("ahmedabbah"))
						.build());

				departementRepo.saveAll(List.of(
						Departement.builder().name("Software Engineering Department").code("SE").build(),
						Departement.builder().name("Research and Development Department").code("R&D").build(),
						Departement.builder().name("Human Resources Department").code("HR").build(),
						Departement.builder().name("Marketing Department").code("MKT").build(),
						Departement.builder().name("Finance Department").code("FIN").build()
				));
			}
		};
	}

}
