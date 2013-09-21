package pl.mjedynak.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import pl.mjedynak.AppConfig;
import pl.mjedynak.model.Account;
import pl.mjedynak.repository.AccountRepository;

import java.math.BigDecimal;

import static java.math.BigDecimal.valueOf;
import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;
import static pl.mjedynak.model.AccountBuilder.anAccount;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = AppConfig.class)
public class TransferServiceIntegrationTest {

    @Autowired private AccountRepository accountRepository;
    @Autowired private TransferService transferService;

    @Test
    public void shouldTransferMoneyFromOneAccountToAnother() {
        // given
        Account from = anAccount().withBalance(valueOf(100)).build();
        Account to = anAccount().withBalance(valueOf(10)).build();
        accountRepository.save(asList(from, to));

        BigDecimal amount = valueOf(60);

        // when
        transferService.transfer(from, to, amount);

        // then
        Account returnedFrom = accountRepository.findOne(from.getId());
        Account returnedTo = accountRepository.findOne(to.getId());
        System.out.println(returnedFrom.getBalance().toEngineeringString());
        assertThat(returnedFrom.getBalance().doubleValue(), is(40d));
        assertThat(returnedTo.getBalance().doubleValue(), is(70d));
    }

    @Test
    public void shouldRollbackTransactionWhenExceptionIsThrown() {
        // given
        Account from = anAccount().withBalance(valueOf(100)).build();
        Account to = anAccount().withBalance(valueOf(0)).build();
        accountRepository.save(asList(from, to));

        BigDecimal amount = valueOf(200);

        // when
        try {
            transferService.transfer(from, to, amount);
        } catch (IllegalStateException e) {
            // then
            Account returnedFrom = accountRepository.findOne(from.getId());
            Account returnedTo = accountRepository.findOne(to.getId());
            assertThat(returnedFrom.getBalance().doubleValue(), is(100d));
            assertThat(returnedTo.getBalance().doubleValue(), is(0d));
            return;
        }
        fail();
    }
}
