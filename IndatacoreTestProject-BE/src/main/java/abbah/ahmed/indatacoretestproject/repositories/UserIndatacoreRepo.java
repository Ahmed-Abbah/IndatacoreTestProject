package abbah.ahmed.indatacoretestproject.repositories;

import abbah.ahmed.indatacoretestproject.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserIndatacoreRepo extends JpaRepository<User, Long> {
    Optional<User>  findByEmail(String email);
}
