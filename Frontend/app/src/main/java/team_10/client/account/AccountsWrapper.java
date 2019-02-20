package team_10.client.account;

import java.util.List;

/*
 * Wrapper class for sending serializing, sending, and
 * receiving accounts.
 */
public class AccountsWrapper {

    private List<Account> accounts;

    public List<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }
}
