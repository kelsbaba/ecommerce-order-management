package com.esegine.ecommerce_order_management.repository;

import com.esegine.ecommerce_order_management.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {


}
