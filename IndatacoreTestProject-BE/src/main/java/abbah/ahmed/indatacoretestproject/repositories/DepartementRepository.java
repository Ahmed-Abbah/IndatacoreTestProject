package abbah.ahmed.indatacoretestproject.repositories;

import abbah.ahmed.indatacoretestproject.entities.Departement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartementRepository extends JpaRepository<Departement, Long> {
    Departement findByCode(String code);

}
