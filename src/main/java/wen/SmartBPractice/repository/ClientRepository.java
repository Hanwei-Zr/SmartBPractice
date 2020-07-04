package wen.SmartBPractice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import wen.SmartBPractice.model.Client;
import wen.SmartBPractice.model.Company;

public interface ClientRepository extends JpaRepository<Client, Long> {
    Client findTopByOrderById();
}
