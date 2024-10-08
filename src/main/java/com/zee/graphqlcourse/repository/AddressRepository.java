package com.zee.graphqlcourse.repository;

import com.zee.graphqlcourse.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @author : Ezekiel Eromosei
 * @code @created : 28 Aug, 2024
 */


public interface AddressRepository extends JpaRepository<Address, UUID>
        , JpaSpecificationExecutor<Address> {

    List<Address> findByEntityId(String id);
    Optional<Address> findByEntityIdAndUuid(String id, UUID uuid);
}
