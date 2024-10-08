package com.zee.graphqlcourse.repository;

import com.zee.graphqlcourse.entity.Outsourced;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @author : Ezekiel Eromosei
 * @code @created : 28 Aug, 2024
 */


public interface OutsourcedRepository extends JpaRepository<Outsourced, UUID>
        , JpaSpecificationExecutor<Outsourced> {

    Optional<Outsourced> findByOutsourceId(String outsourcedId);

    List<Outsourced> findAllByOutsourceIdIn(List<String> outsourcedIds);
}
