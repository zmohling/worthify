package team_10.client;

import org.junit.Test;
import org.mockito.Mock;

import java.time.LocalDate;

import team_10.client.object.account.Account;
import team_10.client.object.account.SavingsAccount;
import team_10.client.object.account.Transaction;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.when;

public class MockTransactionSort {

    @Mock
    Account account;

    @Test
    public void testTransactionSort() {
        SavingsAccount s = new SavingsAccount();
        s.addTransaction(LocalDate.now().plusMonths(2), 1000.0, 0.04, 0);
        s.addTransaction(LocalDate.now().plusMonths(1), 2000.0, 0.04, 0);
        s.addTransaction(LocalDate.now(), 3000.0, 0.04, 0);

        assertEquals((Transaction) s).getTransactions().values().toArray()[0].getValue())
    }

}
