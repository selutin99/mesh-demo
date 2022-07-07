package group.mesh.demo.service;

import group.mesh.demo.domain.dao.Account;
import group.mesh.demo.domain.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DepositSchedulingService {

    private static final String TEN_PERCENT = "0.1";
    private static final String INITIAL_PERCENT = "100";
    private static final String MAX_DEPOSIT_PERCENT = "207";

    private final AccountRepository accountRepository;

    @Async
    @Transactional
    @Scheduled(initialDelay = 5000, fixedRate = 30_000)
    public void percentForDepositAccount() {
        List<Account> accounts = new ArrayList<>();
        accountRepository.findAll().forEach(accounts::add);

        for (Account account: accounts) {
            BigDecimal currentUserAccountBalance = account.getBalance() == null
                    ? account.getInitialBalance()
                    : account.getBalance();
            var oldUserBalance = currentUserAccountBalance.add(BigDecimal.ZERO);
            var newUserBalance = currentUserAccountBalance
                    .add(currentUserAccountBalance.multiply(new BigDecimal(TEN_PERCENT)));
            var maxUserBalance = account.getInitialBalance()
                    .multiply(new BigDecimal(MAX_DEPOSIT_PERCENT))
                    .divide(new BigDecimal(INITIAL_PERCENT), RoundingMode.HALF_UP);

            if (newUserBalance.compareTo(maxUserBalance) > 0) {
                log.info("Maximum deposit percent user(id={}). Old value {}, New value {}; Max balance {}",
                        account.getUser().getId(), oldUserBalance, newUserBalance, maxUserBalance);
                continue;
            }
            account.setBalance(newUserBalance);
            accountRepository.save(account);
            log.info("Pay deposit for user(id={}). Old value {}, New value {}; Max balance {}",
                    account.getUser().getId(), oldUserBalance, newUserBalance, maxUserBalance);
        }
    }
}
