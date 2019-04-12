package team_10.client;

import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import team_10.client.constant.TYPE;
import team_10.client.object.account.Loan;
import team_10.client.object.account.Account;
import team_10.client.object.account.Transaction;

import static org.mockito.Mockito.*;

public class MockTypeEnum {
    // Mock TYPE's testGetFirstMatch
    public void testGetFirstMatch() {
        TYPE type = Mockito.mock(TYPE.class);
        Mockito.when(type.firstMatch("Loan")).thenReturn(TYPE.LOAN);
        Mockito.when(type.firstMatch("SavingsAccount")).thenReturn(TYPE.SAVINGSACCOUNT);
        Mockito.when(type.firstMatch("CertificateOfDeposit")).thenReturn(TYPE.CERTIFICATEOFDEPOSIT);

        verify(type).firstMatch("Loan");
    }

    public void testGetTypeClass() {
        TYPE type = Mockito.mock(TYPE.class);
        Mockito.when(type.getTypeClass()).thenReturn(team_10.client.object.account.Account.class);
        verify(type).getTypeClass();
    }
}
