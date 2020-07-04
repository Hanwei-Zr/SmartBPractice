package wen.SmartBPractice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wen.SmartBPractice.form.ClientForm;
import wen.SmartBPractice.model.Client;
import wen.SmartBPractice.service.ClientService;
import wen.SmartBPractice.util.NotFoundException;
import wen.SmartBPractice.util.Util;

import javax.validation.Valid;
import java.util.List;

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

    @RequestMapping("/create/muti")
    public List<Client> createMutiClient(@Valid @RequestBody List<ClientForm> clientFormList) {
        return clientService.doMutiCreate(clientFormList);
    }


    @RequestMapping("/modify/{id}")
    public Client updateCompany (@PathVariable Long id, @Valid @RequestBody ClientForm clientForm) {
        return clientService.doUpdate(id, clientForm);
    }

    @RequestMapping("/delete/{id}")
    public ResponseEntity<?> deleteCompany (@PathVariable Long id) {
        return clientService.doDelete(id);
    }

    @ExceptionHandler(NotFoundException.class)
    public String handleException(NotFoundException ex) throws JsonProcessingException {
        return Util.mapToJsonString("message", ex.getMessage());
    }
}