package wen.SmartBPractice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import wen.SmartBPractice.model.Company;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {
    Company findByAddress(String address);
    //List<Company> findAll();
}
