package wen.SmartBPractice.repository;

import wen.SmartBPractice.model.Company;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CompanyRepository extends CrudRepository<Company, Long> {
    Company findByAddress(String address);
    List<Company> findAll();
}
