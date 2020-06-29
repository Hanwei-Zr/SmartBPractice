package wen.SmartBPractice.form;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class ClientForm {
    @NotBlank
    private String company_id;

    @NotBlank
    private String name;

    @NotBlank
    private String email;

    @NotBlank(message = "請輸入電話號碼")
    private String phone;
}
