package group.mesh.demo.service;

import group.mesh.demo.domain.dao.Account;
import group.mesh.demo.domain.dao.User;
import group.mesh.demo.domain.repository.AccountRepository;
import group.mesh.demo.domain.repository.UserRepository;
import group.mesh.demo.exception.business.DataValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MoneyTransferServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private MoneyTransferService moneyTransferService;

    private User from;
    private User to;

    @BeforeEach
    void addUsers() {
        // from
        from = new User();
        from.setId(1L);
        from.setDateOfBirth(LocalDate.of(1990, 1, 1));
        from.setName("Test");

        var fromAccount = new Account();
        fromAccount.setBalance(new BigDecimal("100.0"));
        fromAccount.setInitialBalance(new BigDecimal("90.0"));
        fromAccount.setId(1L);
        from.setAccount(fromAccount);
        fromAccount.setUser(from);

        // to
        to = new User();
        to.setId(2L);
        to.setDateOfBirth(LocalDate.of(1990, 1, 1));
        to.setName("Test");

        var toAccount = new Account();
        toAccount.setBalance(new BigDecimal("100.0"));
        toAccount.setInitialBalance(new BigDecimal("90.0"));
        toAccount.setId(2L);
        to.setAccount(toAccount);
        toAccount.setUser(to);
    }

    @Test
    void testTransfer() {
        // given
        when(userService.getUserFromSession()).thenReturn(from);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(to));

        when(accountRepository.save(any(Account.class))).thenReturn(null);

        // when
        moneyTransferService.transfer(1L, new BigDecimal("10.0"));

        // then
        assertEquals(new BigDecimal("110.0"), to.getAccount().getBalance());
        assertEquals(new BigDecimal("90.0"), from.getAccount().getBalance());

        verify(accountRepository, times(2)).save(any(Account.class));
        verifyNoMoreInteractions(accountRepository);
    }

    @Test
    void testTransferWithBigSum() {
        // given
        when(userService.getUserFromSession()).thenReturn(from);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(to));

        // when
        Throwable throwable = assertThrows(Throwable.class, () -> {
            moneyTransferService.transfer(1L, new BigDecimal("1000.0"));
        });

        // then
        assertEquals(DataValidationException.class, throwable.getClass());
        assertEquals("You don't have enough money", throwable.getMessage());

        assertEquals(new BigDecimal("100.0"), to.getAccount().getBalance());
        assertEquals(new BigDecimal("100.0"), from.getAccount().getBalance());

        verifyNoInteractions(accountRepository);
    }

    @Test
    void testTransferWithNullUser() {
        // given
        when(userService.getUserFromSession()).thenReturn(from);
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        // when
        Throwable throwable = assertThrows(Throwable.class, () -> {
            moneyTransferService.transfer(1L, new BigDecimal("10.0"));
        });

        // then
        assertEquals(DataValidationException.class, throwable.getClass());
        assertEquals("Incorrect user", throwable.getMessage());

        verifyNoInteractions(accountRepository);
    }

    @Test
    void testTransferNegativeValue() {
        // given
        when(userService.getUserFromSession()).thenReturn(from);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(to));

        // when
        Throwable throwable = assertThrows(Throwable.class, () -> {
            moneyTransferService.transfer(1L, new BigDecimal("-10.0"));
        });

        // then
        assertEquals(DataValidationException.class, throwable.getClass());
        assertEquals("Value is negative", throwable.getMessage());

        verifyNoInteractions(accountRepository);
    }

    @Test
    void testTransferToYourSelf() {
        // given
        when(userService.getUserFromSession()).thenReturn(from);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(from));

        // when
        Throwable throwable = assertThrows(Throwable.class, () -> {
            moneyTransferService.transfer(1L, new BigDecimal("10.0"));
        });

        // then
        assertEquals(DataValidationException.class, throwable.getClass());
        assertEquals("Transaction to yourself is not available", throwable.getMessage());

        verifyNoInteractions(accountRepository);
    }

    @Test
    void testTransferWithIncorrectToUser() {
        // given
        when(userService.getUserFromSession()).thenReturn(from);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(to));

        // when
        Throwable throwable = assertThrows(Throwable.class, () -> {
            moneyTransferService.transfer(1L, null);
        });

        // then
        assertEquals(DataValidationException.class, throwable.getClass());
        assertEquals("Required fields empty", throwable.getMessage());

        verifyNoInteractions(accountRepository);
    }
}
