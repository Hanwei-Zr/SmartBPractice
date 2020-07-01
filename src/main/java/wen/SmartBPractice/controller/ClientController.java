package wen.SmartBPractice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wen.SmartBPractice.form.ClientForm;
import wen.SmartBPractice.model.Client;
import wen.SmartBPractice.service.ClientService;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/client")
public class ClientController {

    @Autowired
    ClientService clientService;

    @GetMapping("/view")
    public Page<Client> getAllClient (Pageable pageable) {
        return clientService.doGetAll(pageable);
    }

    @RequestMapping("/create")
    public Client createClient(@Valid @RequestBody ClientForm clientForm) {
        return clientService.doCreate(clientForm);
    }


    @RequestMapping("/modify/{id}")
    public Client updateCompany (@PathVariable Long id, @Valid @RequestBody ClientForm clientForm) {
        return clientService.doUpdate(id, clientForm);
    }

    @RequestMapping("/delete/{id}")
    public ResponseEntity<?> deleteCompany (@PathVariable Long id) {
        return clientService.doDelete(id);
    }
}