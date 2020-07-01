package wen.SmartBPractice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import wen.SmartBPractice.model.Client;

public interface ClientRepository extends JpaRepository<Client, Long> {
}
