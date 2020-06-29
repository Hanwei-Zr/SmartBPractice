package wen.SmartBPractice.repository;

import org.springframework.data.repository.CrudRepository;
import wen.SmartBPractice.model.Client;

public interface ClientRepository extends CrudRepository<Client, Long> {

}
