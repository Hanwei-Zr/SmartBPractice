package wen.SmartBPractice.service;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Service;
import wen.SmartBPractice.model.Client;
import wen.SmartBPractice.repository.ClientRepository;

@Service
//@EnableJpaRepositories
@Slf4j
public class ClientCrudService {

    @Autowired
    ClientRepository clientRepository;

    public boolean doCreate (String company_id, String name, String email, String phone) {
        Client client = Client.builder()
                        .company_id(company_id)
                        .name(name)
                        .email(email)
                        .phone(phone)
                        .create_by("wen")
                        .build();

        clientRepository.save(client);

        log.info("Client: {}", client);
        return true;
    }
}

