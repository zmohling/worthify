package team_10.client;

import org.junit.Test;
import org.mockito.Mock;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import team_10.client.object.account.Account;
import team_10.client.object.account.Loan;
import team_10.client.object.account.Transaction;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.when;

public class MockTransactionAccount {
    @Mock
    Account account;
    @Test
    public void testTransactionsAccount() {

        Loan actualLoan = new Loan();
        actualLoan.addTransaction(LocalDate.now(), 1.02, 1.05, 1);

        ArrayList<Transaction> transactions = new ArrayList<>();
        transactions.addAll(actualLoan.getTransactions().values());
        transactions.get(0).setAccount(account);
        when(account.getID()).thenReturn("99");

        assertEquals(transactions.get(0).getDate(), LocalDate.now());
        assertEquals(transactions.get(0).getValue(), 1.02);
        assertEquals(transactions.get(0).getRecurring(), 1);
        assertEquals(transactions.get(0).getAccount().getID(), "99");
    }
}
