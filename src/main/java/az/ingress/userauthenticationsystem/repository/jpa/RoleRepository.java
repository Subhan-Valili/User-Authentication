package az.ingress.userauthenticationsystem.repository.jpa;

import az.ingress.userauthenticationsystem.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role,Long> {
    Optional<Role> findByName(String name);
}
