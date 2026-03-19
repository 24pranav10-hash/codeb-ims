package com.codeb.ims.controller;

import com.codeb.ims.dto.ClientDTOs.*;
import com.codeb.ims.entity.*;
import com.codeb.ims.service.ClientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;

    @PostMapping("/clients")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ClientResponse> createClient(
            @Valid @RequestBody ClientRequest request) {
        return ResponseEntity.ok(clientService.createClient(request));
    }

    @GetMapping("/clients")
    public ResponseEntity<List<ClientResponse>> getAllClients() {
        return ResponseEntity.ok(clientService.getAllClients());
    }

    @GetMapping("/clients/{id}")
    public ResponseEntity<ClientResponse> getClientById(@PathVariable Integer id) {
        return ResponseEntity.ok(clientService.getClientById(id));
    }

    @PutMapping("/clients/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ClientResponse> updateClient(
            @PathVariable Integer id,
            @Valid @RequestBody ClientRequest request) {
        return ResponseEntity.ok(clientService.updateClient(id, request));
    }

    @DeleteMapping("/clients/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteClient(@PathVariable Integer id) {
        clientService.deleteClient(id);
        return ResponseEntity.ok("Client deleted successfully.");
    }

    @GetMapping("/clients/search")
    public ResponseEntity<List<ClientResponse>> searchClients(
            @RequestParam String name) {
        return ResponseEntity.ok(clientService.searchClients(name));
    }

    @GetMapping("/clients/by-chain/{chainId}")
    public ResponseEntity<List<ClientResponse>> getClientsByChain(
            @PathVariable Integer chainId) {
        return ResponseEntity.ok(clientService.getClientsByChain(chainId));
    }

    @PostMapping("/groups")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Group> createGroup(@Valid @RequestBody GroupRequest request) {
        return ResponseEntity.ok(clientService.createGroup(request));
    }

    @GetMapping("/groups")
    public ResponseEntity<List<Group>> getAllGroups() {
        return ResponseEntity.ok(clientService.getAllGroups());
    }

    @PostMapping("/chains")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Chain> createChain(@Valid @RequestBody ChainRequest request) {
        return ResponseEntity.ok(clientService.createChain(request));
    }

    @GetMapping("/chains")
    public ResponseEntity<List<Chain>> getAllChains() {
        return ResponseEntity.ok(clientService.getAllChains());
    }

    @GetMapping("/chains/by-group/{groupId}")
    public ResponseEntity<List<Chain>> getChainsByGroup(@PathVariable Integer groupId) {
        return ResponseEntity.ok(clientService.getChainsByGroup(groupId));
    }

    @PostMapping("/brands")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Brand> createBrand(@Valid @RequestBody BrandRequest request) {
        return ResponseEntity.ok(clientService.createBrand(request));
    }

    @GetMapping("/brands")
    public ResponseEntity<List<Brand>> getAllBrands() {
        return ResponseEntity.ok(clientService.getAllBrands());
    }

    @PostMapping("/subzones")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Subzone> createSubzone(@Valid @RequestBody SubzoneRequest request) {
        return ResponseEntity.ok(clientService.createSubzone(request));
    }

    @GetMapping("/subzones")
    public ResponseEntity<List<Subzone>> getAllSubzones() {
        return ResponseEntity.ok(clientService.getAllSubzones());
    }
}