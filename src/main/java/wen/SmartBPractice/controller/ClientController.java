package wen.SmartBPractice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wen.SmartBPractice.form.ClientForm;
import wen.SmartBPractice.form.CompanyForm;
import wen.SmartBPractice.service.ClientCrudService;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ClientController {

    @Autowired
    ClientCrudService clientCrudService;

    @GetMapping("/admin/hello")
    public Map<String, String> adminSayHello() {

        Map<String, String> map = new HashMap<>();
        map.put("message", "admin say hello");

        return map;
    }

    @RequestMapping("/create")
    public Map<String, String> createCompany(@Valid @RequestBody ClientForm clientForm) {
        Map<String, String> map = new HashMap<>();

        String company_id = clientForm.getCompany_id();
        String name       = clientForm.getName();
        String email      = clientForm.getEmail();
        String phone      = clientForm.getPhone();

        clientCrudService.doCreate(company_id, name, email, phone);

        map.put("message", "Create Success !!");
        return map;
    }
}