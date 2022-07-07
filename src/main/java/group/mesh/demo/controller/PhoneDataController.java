package group.mesh.demo.controller;

import group.mesh.demo.domain.dto.request.phone_data.EditPhoneDataRequestDto;
import group.mesh.demo.domain.dto.request.phone_data.PhoneDataRequestDto;
import group.mesh.demo.domain.dto.response.PhoneDataResponseDto;
import group.mesh.demo.service.PhoneDataService;
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
@RequestMapping("phone")
@RequiredArgsConstructor
@Api(tags = "Phone Data")
public class PhoneDataController {

    private final PhoneDataService phoneDataService;

    @PostMapping(value = "/add")
    @ApiOperation(value = "Add phone data", authorizations = {@Authorization(value = "jwtToken")})
    public PhoneDataResponseDto addPhoneData(@RequestBody PhoneDataRequestDto phone) {
        return phoneDataService.addUserPhoneData(phone.getPhone());
    }

    @PutMapping(value = "/edit")
    @ApiOperation(value = "Edit phone", authorizations = {@Authorization(value = "jwtToken")})
    public PhoneDataResponseDto editPhoneData(@RequestBody EditPhoneDataRequestDto editPhoneData) {
        return phoneDataService.editPhoneData(editPhoneData.getOldPhone(), editPhoneData.getNewPhone());
    }

    @DeleteMapping(value = "/delete")
    @ApiOperation(value = "Delete phone", authorizations = {@Authorization(value = "jwtToken")})
    public PhoneDataResponseDto removePhoneData(@RequestBody PhoneDataRequestDto email) {
        return phoneDataService.removePhoneData(email.getPhone());
    }
}
