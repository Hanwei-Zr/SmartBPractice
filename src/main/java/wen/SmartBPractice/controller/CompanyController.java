package wen.SmartBPractice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wen.SmartBPractice.form.CompanyForm;
import wen.SmartBPractice.model.Company;
import wen.SmartBPractice.service.CompanyService;
import wen.SmartBPractice.util.NotFoundException;
import wen.SmartBPractice.util.Util;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@Api(tags = "Company 管理系統")
@RestController
@RequestMapping("/api/company")
public class CompanyController {

    @Autowired
    private CompanyService companyService;

    @ApiOperation(value = "View all Company", notes = "取得所有Company資訊")
    @ApiResponse(code = 200, message = "成功列出")
    @GetMapping("/view")
    public Page<Company> getAllCompany (Pageable pageable) {
        return companyService.doGetAll(pageable);
    }

    @ApiOperation(value = "Create Company", notes = "建立一個 Company")
    @PostMapping("/create")
    public Company createCompany (@Valid @RequestBody CompanyForm companyForm) {
        Map<String, String> map = new HashMap<>();

        String name = companyForm.getName();
        String address = companyForm.getAddress();

        return companyService.doCreate(name, address);
    }

    @ApiOperation(value = "Modify Company", notes = "更新 Company 資訊")
    @PostMapping("/modify/{id}")
    public Company updateCompany (@PathVariable Long id, @Valid @RequestBody CompanyForm companyForm) {
        return companyService.doUpdate(id, companyForm);
    }

    @ApiOperation(value = "Delete Company", notes = "刪除 Company")
    @GetMapping("/delete/{id}")
    public ResponseEntity deleteCompany (@PathVariable Long id) {
        return companyService.doDelete(id);
    }

    @ExceptionHandler(NotFoundException.class)
    public String handleException(NotFoundException ex) throws JsonProcessingException {
        return Util.mapToJsonString("message", ex.getMessage());
    }
}