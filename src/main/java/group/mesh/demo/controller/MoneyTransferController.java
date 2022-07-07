package group.mesh.demo.controller;

import group.mesh.demo.domain.dto.request.MoneyTransferRequestDto;
import group.mesh.demo.service.MoneyTransferService;
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
public class MoneyTransferController {

    private final MoneyTransferService moneyTransferService;

    @PostMapping
    public ResponseEntity<Object> transferMoney(@RequestBody MoneyTransferRequestDto moneyTransferRequest) {
        moneyTransferService.transfer(moneyTransferRequest.getTo(), moneyTransferRequest.getValue());
        return ResponseEntity.status(HttpStatus.OK).body("OK");
    }
}
