package pl.mjedynak.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pl.mjedynak.model.Account;
import pl.mjedynak.repository.AccountRepository;

import java.math.BigDecimal;

@Component
public class TransferService {

    @Autowired private AccountRepository accountRepository;

    @Transactional
    public void transfer(Account from, Account to, BigDecimal amount) {
        BigDecimal currentFromBalance = from.getBalance();
        BigDecimal currentToBalance = to.getBalance();

        to.setBalance(currentToBalance.add(amount));
        accountRepository.save(to);

        if (currentFromBalance.compareTo(amount) < 0) {
            throw new IllegalStateException("not enough money");
        }

        from.setBalance(currentFromBalance.subtract(amount));
        accountRepository.save(from);
    }
}
