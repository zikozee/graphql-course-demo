package com.zee.graphqlcourse.repository;

import com.zee.graphqlcourse.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

/**
 * @author : Ezekiel Eromosei
 * @code @created : 28 Aug, 2024
 */

// todo explain to use only what is needed, CrudRepo... if only CRUD, PagingSorting if only paging and Sorting
public interface CompanyRepository extends JpaRepository<Company, UUID>
        , JpaSpecificationExecutor<Company> {
}
