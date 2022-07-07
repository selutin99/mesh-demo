package group.mesh.demo.controller;

import group.mesh.demo.domain.dto.request.email_data.EditEmailDataRequestDto;
import group.mesh.demo.domain.dto.request.email_data.EmailDataRequestDto;
import group.mesh.demo.domain.dto.response.EmailDataResponseDto;
import group.mesh.demo.service.EmailDataService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("email")
@RequiredArgsConstructor
@Api(tags = "Email Data")
public class EmailDataController {

    private final EmailDataService emailDataService;

    @PostMapping(value = "/add")
    @ApiOperation(value = "Add email data", authorizations = {@Authorization(value = "jwtToken")})
    public EmailDataResponseDto addEmailData(@RequestBody EmailDataRequestDto email) {
        return emailDataService.addUserEmailData(email.getEmail());
    }

    @PutMapping(value = "/edit")
    @ApiOperation(value = "Edit email data", authorizations = {@Authorization(value = "jwtToken")})
    public EmailDataResponseDto editEmailData(@RequestBody EditEmailDataRequestDto editEmailData) {
        return emailDataService.editEmailData(editEmailData.getOldEmail(), editEmailData.getNewEmail());
    }

    @DeleteMapping(value = "/delete")
    @ApiOperation(value = "Remove email data", authorizations = {@Authorization(value = "jwtToken")})
    public EmailDataResponseDto removeEmailData(@RequestBody EmailDataRequestDto email) {
        return emailDataService.removeEmailData(email.getEmail());
    }
}
