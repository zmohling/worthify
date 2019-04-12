package team_10.client;

import org.junit.Test;
import org.mockito.Mock;

import java.time.LocalDate;
import java.util.ArrayList;

import team_10.client.object.account.Loan;
import team_10.client.object.account.Transaction;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.when;

public class MockLoanValue {
    @Mock
    Loan loan;
    @Test
    public void testTransactionsAccount() {

        Loan actualLoan = new Loan();
        actualLoan.addTransaction(LocalDate.now().minusMonths(1), 100, 0.05, 0);
        ArrayList<Transaction> transactions = new ArrayList<>();
        transactions.addAll(actualLoan.getTransactions().values());
        transactions.get(0).setAccount(loan);
        when(loan.getValue(LocalDate.now())).thenReturn(105.13);

        assertEquals(transactions.get(0).getDate(), LocalDate.now().minusMonths(1));
        assertEquals(transactions.get(0).getValue(), 100);
        assertEquals(transactions.get(0).getRecurring(), 0);
        assertEquals(transactions.get(0).getAccount().getValue(LocalDate.now()), 105.13);
    }
}
