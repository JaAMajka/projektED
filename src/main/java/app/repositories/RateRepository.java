package app.repositories;


import app.models.Rate;
import app.projections.GlobalStatsRateProjection;
import app.projections.RateExportProjection;
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
            "AVG(r.atmosphereScore) AS avgAtmosphereScore, STDDEV(r.atmosphereScore) AS stdDevAtmosphere, " +
            "AVG(r.beverageScore) AS avgBeverageScore, STDDEV(r.beverageScore) AS stdDevBeverage, " +
            "AVG(r.serviceScore) AS avgServiceScore, STDDEV(r.serviceScore) AS stdDevService " +
            "FROM Rate r WHERE r.cafe.id = :cafeId")
    Optional<StatsRateProjection> findStatsByRate(Long cafeId);
    @Query("SELECT " +
            "r.cafe.id AS cafeId," +
            "AVG(r.atmosphereScore) AS avgAtmosphereScore, STDDEV(r.atmosphereScore) AS stdDevAtmosphere, " +
            "AVG(r.beverageScore) AS avgBeverageScore, STDDEV(r.beverageScore) AS stdDevBeverage, " +
            "AVG(r.serviceScore) AS avgServiceScore, STDDEV(r.serviceScore) AS stdDevService " +
            "FROM Rate r GROUP BY r.cafe.id")
    List<GlobalStatsRateProjection> findStatsForAllRates();
    @Query("SELECT r.id AS rateId, r.author.id AS userId, r.cafe.id AS cafeId, " +
            "r.beverageScore AS beverageScore, r.serviceScore AS serviceScore, r.atmosphereScore AS atmosphereScore, " +
            "r.author.profile AS userProfile " +
            "FROM Rate r WHERE r.author.profile IS NOT NULL")
    List<RateExportProjection> findAllForExport();
}
