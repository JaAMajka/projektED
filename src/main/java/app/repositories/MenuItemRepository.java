package app.repositories;


import app.models.Cafe;
import app.models.MenuItem;
import app.models.Schedule;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

@Repository
public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {

    Optional<MenuItem> findByCafeAndName(Cafe cafe, String name);
    List<MenuItem> findAllByCafeId(Long cafeId);
}
