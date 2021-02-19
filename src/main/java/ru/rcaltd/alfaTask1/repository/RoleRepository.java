package ru.rcaltd.alfaTask1.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.rcaltd.alfaTask1.entity.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

}
