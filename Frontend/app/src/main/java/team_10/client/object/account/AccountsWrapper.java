package team_10.client.object.account;

import java.util.List;

import team_10.client.data.models.Account;

/*
 * Wrapper class for sending serializing, sending, and
 * receiving accounts.
 */
public class AccountsWrapper {

    public AccountsWrapper() { }

    public AccountsWrapper(List<Account> accounts) {
        setAccounts(accounts);
    }

    private List<Account> accounts;

    public List<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }
}
