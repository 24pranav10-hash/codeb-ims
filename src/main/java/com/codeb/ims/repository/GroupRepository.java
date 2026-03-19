package com.codeb.ims.repository;

import com.codeb.ims.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface GroupRepository extends JpaRepository<Group, Integer> {
    List<Group> findByGroupNameContainingIgnoreCase(String name);
}