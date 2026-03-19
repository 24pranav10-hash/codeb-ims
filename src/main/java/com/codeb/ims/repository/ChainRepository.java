package com.codeb.ims.repository;

import com.codeb.ims.entity.Chain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ChainRepository extends JpaRepository<Chain, Integer> {
    List<Chain> findByGroupGroupId(Integer groupId);
}