package app.repositories;
import app.models.GlobalStats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface GlobalStatsReadModelRepository extends JpaRepository<GlobalStats, Long>, JpaSpecificationExecutor<GlobalStats> {
}
