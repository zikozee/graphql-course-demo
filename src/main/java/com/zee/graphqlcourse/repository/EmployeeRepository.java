package com.zee.graphqlcourse.repository;

import com.zee.graphqlcourse.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;
import java.util.UUID;

/**
 * @author : Ezekiel Eromosei
 * @code @created : 28 Aug, 2024
 */


public interface EmployeeRepository extends JpaRepository<Employee, UUID>
        , JpaSpecificationExecutor<Employee> {

    Optional<Employee> findByEmployeeId(String employeeId);
}
