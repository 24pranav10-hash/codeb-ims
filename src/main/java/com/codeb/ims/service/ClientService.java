package com.codeb.ims.service;

import com.codeb.ims.dto.ClientDTOs.*;
import com.codeb.ims.entity.*;
import com.codeb.ims.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository  clientRepository;
    private final GroupRepository   groupRepository;
    private final ChainRepository   chainRepository;
    private final BrandRepository   brandRepository;
    private final SubzoneRepository subzoneRepository;
    private final UserRepository    userRepository;

    public ClientResponse createClient(ClientRequest req) {
        Client client = new Client();
        mapRequestToClient(req, client);
        return toResponse(clientRepository.save(client));
    }

    public List<ClientResponse> getAllClients() {
        return clientRepository.findAll()
                .stream().map(this::toResponse)
                .collect(Collectors.toList());
    }

    public ClientResponse getClientById(Integer id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Client not found with id: " + id));
        return toResponse(client);
    }

    public ClientResponse updateClient(Integer id, ClientRequest req) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Client not found with id: " + id));
        mapRequestToClient(req, client);
        return toResponse(clientRepository.save(client));
    }

    public void deleteClient(Integer id) {
        if (!clientRepository.existsById(id)) {
            throw new RuntimeException("Client not found with id: " + id);
        }
        clientRepository.deleteById(id);
    }

    public List<ClientResponse> searchClients(String name) {
        return clientRepository.findByClientNameContainingIgnoreCase(name)
                .stream().map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<ClientResponse> getClientsByChain(Integer chainId) {
        return clientRepository.findByChainChainId(chainId)
                .stream().map(this::toResponse)
                .collect(Collectors.toList());
    }

    public Group createGroup(GroupRequest req) {
        Group group = new Group();
        group.setGroupName(req.getGroupName());
        group.setDescription(req.getDescription());
        return groupRepository.save(group);
    }

    public List<Group> getAllGroups() {
        return groupRepository.findAll();
    }

    public Chain createChain(ChainRequest req) {
        Chain chain = new Chain();
        chain.setChainName(req.getChainName());
        if (req.getGroupId() != null) {
            Group group = groupRepository.findById(req.getGroupId())
                    .orElseThrow(() -> new RuntimeException("Group not found"));
            chain.setGroup(group);
        }
        return chainRepository.save(chain);
    }

    public List<Chain> getAllChains() {
        return chainRepository.findAll();
    }

    public List<Chain> getChainsByGroup(Integer groupId) {
        return chainRepository.findByGroupGroupId(groupId);
    }

    public Brand createBrand(BrandRequest req) {
        Brand brand = new Brand();
        brand.setBrandName(req.getBrandName());
        if (req.getChainId() != null) {
            Chain chain = chainRepository.findById(req.getChainId())
                    .orElseThrow(() -> new RuntimeException("Chain not found"));
            brand.setChain(chain);
        }
        return brandRepository.save(brand);
    }

    public List<Brand> getAllBrands() {
        return brandRepository.findAll();
    }

    public Subzone createSubzone(SubzoneRequest req) {
        Subzone subzone = new Subzone();
        subzone.setSubzoneName(req.getSubzoneName());
        subzone.setRegion(req.getRegion());
        return subzoneRepository.save(subzone);
    }

    public List<Subzone> getAllSubzones() {
        return subzoneRepository.findAll();
    }

    private void mapRequestToClient(ClientRequest req, Client client) {
        client.setClientName(req.getClientName());
        client.setContactName(req.getContactName());
        client.setEmail(req.getEmail());
        client.setPhone(req.getPhone());
        client.setGstin(req.getGstin());
        client.setAddress(req.getAddress());

        if (req.getGroupId() != null)
            client.setGroup(groupRepository.findById(req.getGroupId()).orElse(null));
        if (req.getChainId() != null)
            client.setChain(chainRepository.findById(req.getChainId()).orElse(null));
        if (req.getBrandId() != null)
            client.setBrand(brandRepository.findById(req.getBrandId()).orElse(null));
        if (req.getSubzoneId() != null)
            client.setSubzone(subzoneRepository.findById(req.getSubzoneId()).orElse(null));
        if (req.getAssignedTo() != null)
            client.setAssignedTo(userRepository.findById(req.getAssignedTo()).orElse(null));
    }

    private ClientResponse toResponse(Client c) {
        ClientResponse res = new ClientResponse();
        res.setClientId(c.getClientId());
        res.setClientName(c.getClientName());
        res.setContactName(c.getContactName());
        res.setEmail(c.getEmail());
        res.setPhone(c.getPhone());
        res.setGstin(c.getGstin());
        res.setAddress(c.getAddress());
        res.setStatus(c.getStatus().name());
        res.setCreatedAt(c.getCreatedAt());

        if (c.getGroup()      != null) res.setGroupName(c.getGroup().getGroupName());
        if (c.getChain()      != null) res.setChainName(c.getChain().getChainName());
        if (c.getBrand()      != null) res.setBrandName(c.getBrand().getBrandName());
        if (c.getSubzone()    != null) res.setSubzoneName(c.getSubzone().getSubzoneName());
        if (c.getAssignedTo() != null) res.setAssignedToName(c.getAssignedTo().getFullName());

        return res;
    }
}