package com.codeb.ims.service;

import com.codeb.ims.dto.EstimateDTOs.*;
import com.codeb.ims.entity.*;
import com.codeb.ims.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EstimateService {

    private final EstimateRepository estimateRepository;
    private final ClientRepository   clientRepository;
    private final ChainRepository    chainRepository;
    private final UserRepository     userRepository;

    public EstimateResponse createEstimate(EstimateRequest req, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found: " + userEmail));
        Client client = clientRepository.findById(req.getClientId())
                .orElseThrow(() -> new RuntimeException("Client not found"));

        Estimate estimate = new Estimate();
        estimate.setEstimateNumber("EST-" + System.currentTimeMillis());
        estimate.setClient(client);
        estimate.setCreatedBy(user);
        estimate.setTaxPercent(req.getTaxPercent() != null ? req.getTaxPercent() : new BigDecimal("18.00"));
        estimate.setValidUntil(req.getValidUntil());
        estimate.setNotes(req.getNotes());

        if (req.getChainId() != null)
            chainRepository.findById(req.getChainId()).ifPresent(estimate::setChain);

        // Build line items
        List<EstimateItem> items = req.getItems().stream().map(i -> {
            EstimateItem item = new EstimateItem();
            item.setEstimate(estimate);
            item.setDescription(i.getDescription());
            item.setQuantity(i.getQuantity());
            item.setUnitPrice(i.getUnitPrice());
            item.setTotalPrice(i.getQuantity().multiply(i.getUnitPrice()));
            return item;
        }).collect(Collectors.toList());
        estimate.setItems(items);

        // Calculate totals
        BigDecimal subtotal = items.stream()
                .map(EstimateItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal taxAmount = subtotal.multiply(estimate.getTaxPercent())
                .divide(new BigDecimal("100"));
        estimate.setSubtotal(subtotal);
        estimate.setTaxAmount(taxAmount);
        estimate.setTotalAmount(subtotal.add(taxAmount));

        return toResponse(estimateRepository.save(estimate));
    }

    public List<EstimateResponse> getAllEstimates() {
        return estimateRepository.findAll().stream()
                .map(this::toResponse).collect(Collectors.toList());
    }

    public EstimateResponse getEstimateById(Integer id) {
        return toResponse(estimateRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Estimate not found")));
    }

    public EstimateResponse updateStatus(Integer id, String status) {
        Estimate estimate = estimateRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Estimate not found"));
        estimate.setStatus(Estimate.Status.valueOf(status));
        return toResponse(estimateRepository.save(estimate));
    }

    public void deleteEstimate(Integer id) {
        estimateRepository.deleteById(id);
    }

    private EstimateResponse toResponse(Estimate e) {
        EstimateResponse res = new EstimateResponse();
        res.setEstimateId(e.getEstimateId());
        res.setEstimateNumber(e.getEstimateNumber());
        res.setClientName(e.getClient().getClientName());
        res.setCreatedByName(e.getCreatedBy().getFullName());
        res.setSubtotal(e.getSubtotal());
        res.setTaxPercent(e.getTaxPercent());
        res.setTaxAmount(e.getTaxAmount());
        res.setTotalAmount(e.getTotalAmount());
        res.setStatus(e.getStatus().name());
        res.setValidUntil(e.getValidUntil());
        res.setNotes(e.getNotes());
        res.setCreatedAt(e.getCreatedAt());
        if (e.getChain() != null) res.setChainName(e.getChain().getChainName());
        if (e.getItems() != null) {
            res.setItems(e.getItems().stream().map(i -> {
                EstimateItemResponse ir = new EstimateItemResponse();
                ir.setItemId(i.getItemId());
                ir.setDescription(i.getDescription());
                ir.setQuantity(i.getQuantity());
                ir.setUnitPrice(i.getUnitPrice());
                ir.setTotalPrice(i.getTotalPrice());
                return ir;
            }).collect(Collectors.toList()));
        }
        return res;
    }
}