package team_10.client;

import org.junit.Test;

import java.time.LocalDate;
import java.util.ArrayList;

import team_10.client.data.models.Account;
import team_10.client.data.models.Loan;
import team_10.client.data.models.Transaction;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MockTransactionAccount {
    @Test
    public void testTransactionsAccount() {
        Account mock = mock(Account.class);
        Loan actualLoan = new Loan();
        actualLoan.addTransaction(LocalDate.now(), 1.02, 1.05, 1);

        ArrayList<Transaction> transactions = new ArrayList<>();
        transactions.addAll(actualLoan.getTransactions().values());
        when(mock.getID()).thenReturn("99");
        transactions.get(0).setAccount(mock);

        assertEquals(transactions.get(0).getDate(), LocalDate.now());
        assertEquals(transactions.get(0).getValue(), 1.02);
        assertEquals(transactions.get(0).getRecurring(), 1);
        assertEquals(transactions.get(0).getAccount().getID(), "99");
    }
}
