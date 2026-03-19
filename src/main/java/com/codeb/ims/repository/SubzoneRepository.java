package com.codeb.ims.repository;

import com.codeb.ims.entity.Subzone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SubzoneRepository extends JpaRepository<Subzone, Integer> {
    List<Subzone> findByRegionIgnoreCase(String region);
}