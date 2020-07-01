package wen.SmartBPractice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import wen.SmartBPractice.form.CompanyForm;
import wen.SmartBPractice.model.Company;
import wen.SmartBPractice.repository.CompanyRepository;
import wen.SmartBPractice.util.NotFoundException;

@Service
public class CompanyService {

    @Autowired
    CompanyRepository companyRepository;

    public Page<Company> doGetAll (Pageable pageable) {
        return companyRepository.findAll(pageable);
    }

    public Company doCreate (String name, String address) {
        Company company = Company.builder().name(name)
                        .address(address).create_by(getAuthName())
                        .build();
        return companyRepository.save(company);
    }

    public Company doUpdate (Long id, CompanyForm companyForm) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("CompanyId " + id + " not found"));
        company.setName(companyForm.getName());
        company.setAddress(companyForm.getAddress());
        company.setUpdate_by(getAuthName());
        return companyRepository.save(company);
    }

    public ResponseEntity<?> doDelete (Long id) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("CompanyId " + id + " not found"));
        companyRepository.delete(company);
        return ResponseEntity.ok().build();
    }

    public String getAuthName () {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication auth = context.getAuthentication();
        return auth.getName();
    }
}
