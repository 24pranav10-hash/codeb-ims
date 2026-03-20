package com.codeb.ims.repository;

import com.codeb.ims.entity.Estimate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface EstimateRepository extends JpaRepository<Estimate, Integer> {
    List<Estimate> findByClientClientId(Integer clientId);
    List<Estimate> findByCreatedByUserId(Integer userId);
    List<Estimate> findByStatus(Estimate.Status status);
}