package com.zee.graphqlcourse.repository;

import com.zee.graphqlcourse.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

/**
 * @author : Ezekiel Eromosei
 * @code @created : 28 Aug, 2024
 */


public interface DepartmentRepository extends JpaRepository<Department, UUID>
        , JpaSpecificationExecutor<Department> {
}
