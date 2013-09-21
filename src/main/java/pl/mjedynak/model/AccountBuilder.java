package pl.mjedynak.model;

import java.math.BigDecimal;

public class AccountBuilder {
    private Long id;
    private BigDecimal balance;

    private AccountBuilder() {
    }

    public static AccountBuilder anAccount() {
        return new AccountBuilder();
    }

    public AccountBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public AccountBuilder withBalance(BigDecimal balance) {
        this.balance = balance;
        return this;
    }

    public Account build() {
        Account account = new Account();
        account.setId(id);
        account.setBalance(balance);
        return account;
    }
}
