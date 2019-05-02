package team_10.client;

import org.mockito.Mockito;

import team_10.client.constant.TYPE;
import team_10.client.data.models.Account;

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
        Mockito.when(type.getTypeClass()).thenReturn(Account.class);
        verify(type).getTypeClass();
    }
}
