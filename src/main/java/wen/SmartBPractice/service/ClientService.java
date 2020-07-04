package wen.SmartBPractice.service;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wen.SmartBPractice.form.ClientForm;
import wen.SmartBPractice.model.Client;
import wen.SmartBPractice.model.Company;
import wen.SmartBPractice.repository.ClientRepository;
import wen.SmartBPractice.repository.CompanyRepository;
import wen.SmartBPractice.util.NotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@Transactional
public class ClientService {

    @Autowired
    ClientRepository clientRepository;

    @Autowired
    CompanyRepository companyRepository;

    public Client getClient () {
        return null;
    }

    public Page<Client> doGetAll (Pageable pageable) {
        return clientRepository.findAll(pageable);
    }

    public Client findCompanyBuildClient(ClientForm clientForm) {
        Long findId = Long.parseLong(clientForm.getCompany_id());
        Optional<Company> companyOpt = companyRepository.findById(findId);
        Company company = companyOpt.orElseThrow(() ->
                new NotFoundException("companyId " + clientForm.getCompany_id() + " not found"));

        Client client = Client.builder().company(company)
                .name(clientForm.getName())
                .email(clientForm.getEmail())
                .phone(clientForm.getPhone())
                .create_by(getAuthName())
                .build();
        return client;
    }

    public Client doCreate (ClientForm clientForm) {
        Client c = findCompanyBuildClient(clientForm);
        return clientRepository.save(c);
    }

    public List<Client> doMutiCreate(List<ClientForm> clientFormList) {
        List<Client> clients = new ArrayList<>();

        clientFormList.stream().forEach( client -> {
            Client c = findCompanyBuildClient(client);
            clientRepository.save(c);
            clients.add(c);
        });
        return clients;
    }

    public Client doUpdate (Long id, ClientForm clientForm) {
        Long findId = Long.parseLong(clientForm.getCompany_id());

        if(!companyRepository.existsById(findId)) {
            throw new NotFoundException("Company id " + findId + " not found");
        }

        return clientRepository.findById(id).map(client -> {
            client.setEmail(clientForm.getEmail());
            client.setName(clientForm.getName());
            client.setPhone(clientForm.getPhone());
            client.setUpdate_by(getAuthName());
            return clientRepository.save(client);
        }).orElseThrow(() -> new NotFoundException("Client id " + id + "not found"));
    }

    public ResponseEntity<?> doDelete (Long id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Client Id " + id + " not found"));
        clientRepository.delete(client);
        return ResponseEntity.ok().build();
    }

    public String getAuthName () {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication auth = context.getAuthentication();
        return auth.getName();
    }
}

