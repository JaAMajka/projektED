package app.repositories;


import app.models.Rate;
import app.projections.StatsRateProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface RateRepository extends JpaRepository<Rate, Long> {
    List<Rate> findAllByCafeId(Long cafeId);

    @Query("SELECT " +
            "AVG(r.atmosphereScore) AS avgAtmosphere, STDDEV(r.atmosphereScore) AS stdDevAtmosphere, " +
            "AVG(r.beverageScore) AS avgBeverage, STDDEV(r.beverageScore) AS stdDevBeverage, " +
            "AVG(r.serviceScore) AS avgService, STDDEV(r.serviceScore) AS stdDevService " +
            "FROM Rate r WHERE r.cafe.id = :cafeId")
    Optional<StatsRateProjection> findStatsByRate(Long cafeId);
}
