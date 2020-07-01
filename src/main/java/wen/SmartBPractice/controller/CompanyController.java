package wen.SmartBPractice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wen.SmartBPractice.form.CompanyForm;
import wen.SmartBPractice.model.Company;
import wen.SmartBPractice.service.CompanyService;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/company")
public class CompanyController {

    @Autowired
    private CompanyService companyService;

    @GetMapping("/view")
    public Page<Company> getAllCompany (Pageable pageable) {
        return companyService.doGetAll(pageable);
    }

    @RequestMapping("/create")
    public Company createCompany (@Valid @RequestBody CompanyForm companyForm) {
        Map<String, String> map = new HashMap<>();

        String name = companyForm.getName();
        String address = companyForm.getAddress();

        return companyService.doCreate(name, address);
    }

    @RequestMapping("/modify/{id}")
    public Company updateCompany (@PathVariable Long id, @Valid @RequestBody CompanyForm companyForm) {
        return companyService.doUpdate(id, companyForm);
    }

    @RequestMapping("/delete/{id}")
    public ResponseEntity<?> deleteCompany (@PathVariable Long id) {
        return companyService.doDelete(id);
    }
}