package com.shopsphere.order;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface OrderRepository extends JpaRepository<CustomerOrder,Long>{ List<CustomerOrder> findByUserIdOrderByCreatedAtDesc(Long userId); }
