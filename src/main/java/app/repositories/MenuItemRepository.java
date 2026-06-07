package app.repositories;


import app.models.Cafe;
import app.models.MenuItem;
import app.projections.AvgPriceProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

@Repository
public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {

    Optional<MenuItem> findByCafeAndName(Cafe cafe, String name);
    List<MenuItem> findAllByCafeId(Long cafeId);
    @Query("SELECT AVG(m.price), m.type FROM MenuItem m WHERE m.cafe.id = :cafeId AND m.isAppendage = false GROUP BY m.type")
    List<AvgPriceProjection> findAvgPricePerType(Long cafeId);
    @Query("SELECT EXISTS( SELECT 1 FROM MenuItem m WHERE m.isIced = true AND m.isAppendage = false AND m.cafe.id = :cafeId)")
    Boolean checkIfCafeHasIcedItems(Long cafeId);
    @Query("SELECT AVG(m.price), m.type FROM MenuItem m WHERE m.isAppendage = false GROUP BY m.type")
    List<AvgPriceProjection> findAvgPricePerTypeGlobal();

}
