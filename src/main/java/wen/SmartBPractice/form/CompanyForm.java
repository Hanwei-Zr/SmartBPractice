package wen.SmartBPractice.form;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class CompanyForm {
    @NotBlank
    private String name;

    @NotBlank
    private String address;
}
