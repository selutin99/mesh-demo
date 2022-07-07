package group.mesh.demo.controller;

import group.mesh.demo.domain.dto.request.UserCredentialsDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(tags = "Login controller")
public class LoginController {

    @PostMapping(value = "/login")
    @ApiOperation(value = "Login")
    public void login(@RequestBody UserCredentialsDto credentials) {
        // group.mesh.demo.config.security.filter.LoginFilter make a authentication
        // This class use only for Swagger
    }
}
