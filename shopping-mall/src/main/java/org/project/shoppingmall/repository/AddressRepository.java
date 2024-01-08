package org.project.shoppingmall.repository;

import org.project.shoppingmall.domain.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {
}
