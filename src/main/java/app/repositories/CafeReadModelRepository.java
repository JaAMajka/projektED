package app.repositories;


import app.models.CafeReadModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface CafeReadModelRepository extends JpaRepository<CafeReadModel, Long>, JpaSpecificationExecutor<CafeReadModel> {
    Optional<CafeReadModel> findByCafeId(Long id);
}
