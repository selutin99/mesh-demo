package group.mesh.demo.controller;

import group.mesh.demo.domain.dto.request.MoneyTransferRequestDto;
import group.mesh.demo.service.MoneyTransferService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("transfer")
@RequiredArgsConstructor
@Api(tags = "Money Transfer")
public class MoneyTransferController {

    private final MoneyTransferService moneyTransferService;

    @PostMapping
    @ApiOperation(value = "Transfer money", authorizations = {@Authorization(value = "jwtToken")})
    public ResponseEntity<Object> transferMoney(@RequestBody MoneyTransferRequestDto moneyTransferRequest) {
        moneyTransferService.transfer(moneyTransferRequest.getTo(), moneyTransferRequest.getValue());
        return ResponseEntity.status(HttpStatus.OK).body("OK");
    }
}
