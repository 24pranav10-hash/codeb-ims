package com.codeb.ims.repository;

import com.codeb.ims.entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Integer> {
    List<Invoice> findByClientClientId(Integer clientId);
    List<Invoice> findByStatus(Invoice.Status status);
    List<Invoice> findByCreatedByUserId(Integer userId);
}