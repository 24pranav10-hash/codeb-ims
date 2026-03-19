package com.codeb.ims.repository;

import com.codeb.ims.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ClientRepository extends JpaRepository<Client, Integer> {

    List<Client> findByStatus(Client.Status status);

    List<Client> findByClientNameContainingIgnoreCase(String name);

    List<Client> findByAssignedToUserId(Integer userId);

    List<Client> findByChainChainId(Integer chainId);

    List<Client> findByGroupGroupId(Integer groupId);
}