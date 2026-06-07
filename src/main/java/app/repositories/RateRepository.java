package app.repositories;


import app.models.Rate;
import app.projections.AvgRateProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface RateRepository extends JpaRepository<Rate, Long> {
    List<Rate> findAllByCafeId(Long cafeId);

    @Query("SELECT AVG(r.atmosphereScore), AVG(r.beverageScore), AVG(r.serviceScore) FROM Rate r WHERE r.cafe.id = :cafeId")
    Optional<AvgRateProjection> findAvgRate(Long cafeId);
}
