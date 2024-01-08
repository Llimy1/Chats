package org.project.shoppingmall.repository;

import org.project.shoppingmall.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
