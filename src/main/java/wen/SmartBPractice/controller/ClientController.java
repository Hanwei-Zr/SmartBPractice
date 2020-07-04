package wen.SmartBPractice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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

@Api(tags = "Client 管理系統")
@RestController
@RequestMapping("/api/client")
public class ClientController {

    @Autowired
    ClientService clientService;

    @ApiOperation(value = "View all Client", notes = "取得所有 Client 資訊")
    @GetMapping("/view")
    public Page<Client> getAllClient (Pageable pageable) {
        return clientService.doGetAll(pageable);
    }

    @ApiOperation(value = "Create Client", notes = "建立一個 Client 資訊")
    @PostMapping("/create")
    public Client createClient(@Valid @RequestBody ClientForm clientForm) {
        return clientService.doCreate(clientForm);
    }

    @ApiOperation(value = "Create Multiple Client", notes = "建立多個 Client 資訊")
    @PostMapping("/create/multi")
    public List<Client> createMultiClient(@Valid @RequestBody List<ClientForm> clientFormList) {
        return clientService.doMultiCreate(clientFormList);
    }

    @ApiOperation(value = "Modify Client", notes = "更改一個 Client 資訊")
    @PostMapping("/modify/{id}")
    public Client updateCompany (@PathVariable Long id, @Valid @RequestBody ClientForm clientForm) {
        return clientService.doUpdate(id, clientForm);
    }

    @ApiOperation(value = "Delete Client", notes = "刪除一個 Client 資訊")
    @GetMapping("/delete/{id}")
    public ResponseEntity<?> deleteCompany (@PathVariable Long id) {
        return clientService.doDelete(id);
    }

    @ExceptionHandler(NotFoundException.class)
    public String handleException(NotFoundException ex) throws JsonProcessingException {
        return Util.mapToJsonString("message", ex.getMessage());
    }
}