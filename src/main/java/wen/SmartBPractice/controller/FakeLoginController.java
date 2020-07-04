package wen.SmartBPractice.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

@Api(tags = "登入 Api")
@RestController
@RequestMapping("/api")
public class FakeLoginController {
    @ApiOperation("Login.")
    @PostMapping("/login")
    public void fakeLogin(@ApiParam("account") @RequestParam String account, @ApiParam("password") @RequestParam String password) {
        throw new IllegalStateException("This method shouldn't be called. It's implemented by Spring Security filters.");
    }

    @ApiOperation("Logout.")
    @GetMapping("/logout")
    public void fakeLogout() {
        throw new IllegalStateException("This method shouldn't be called. It's implemented by Spring Security filters.");
    }
}
