package se.sofiekl.macromealplanner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.sofiekl.macromealplanner.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
