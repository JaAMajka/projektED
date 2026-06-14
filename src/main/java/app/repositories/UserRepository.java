package app.repositories;


import app.Role;
import app.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findUserByEmail(String mail);
    Optional<User> findUserByPhoneNumber(String phoneNumber);
    long countAllByRoleIsLike(Role role);
    List<User> findTop100ByRoleOrderByCreatedAtAsc(Role role);

    double countAllByRole(Role role);
}
