package group.mesh.demo.service;

import group.mesh.demo.domain.dao.User;
import group.mesh.demo.domain.repository.AccountRepository;
import group.mesh.demo.domain.repository.UserRepository;
import group.mesh.demo.exception.business.DataValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class MoneyTransferService {

    private final UserService userService;

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;

    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public void transfer(Long to, BigDecimal value) {
        User fromUser = userService.getUserFromSession();
        User toUser = userRepository.findById(to).orElseThrow(() -> new DataValidationException("Incorrect user"));
        // Validations
        validateTransfer(fromUser, toUser, value);
        // Calculate new balances
        var newBalanceTo = toUser.getAccount().getBalance().add(value);
        var newBalanceFrom = fromUser.getAccount().getBalance().subtract(value);
        // Set new balance to accounts
        var toAccount = toUser.getAccount();
        toAccount.setBalance(newBalanceTo);
        var fromAccount = fromUser.getAccount();
        fromAccount.setBalance(newBalanceFrom);
        // Save new balances
        accountRepository.save(toAccount);
        accountRepository.save(fromAccount);
    }

    private void validateTransfer(User from, User to, BigDecimal value) {
        if (to == null || from == null || value == null) {
            throw new DataValidationException("Required fields empty");
        }
        // Check value is positive
        if (value.compareTo(new BigDecimal("0.0")) < 1) {
            throw new DataValidationException("Value is negative");
        }
        // Check from balance minus value is positive
        var fromBalance = from.getAccount().getBalance() == null
                ? from.getAccount().getInitialBalance()
                : from.getAccount().getBalance();
        if (fromBalance.subtract(value).compareTo(new BigDecimal("0.0")) < 1) {
            throw new DataValidationException("You don't have enough money");
        }
        // Check to account is not the same as from account
        if (from.getId().equals(to.getId())) {
            throw new DataValidationException("Transaction to yourself is not available");
        }
    }
}
