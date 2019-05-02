package team_10.client;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import team_10.client.data.User;
import team_10.client.data.models.Account;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MockUserAccounts {
    @Test
    public void testAccountsForUser() {
        User user = new User(1, "Marek", "Kyle", "test", 0, "token");
        List<Account> list = new ArrayList<Account>();
        Account mock = mock(Account.class);
        list.add(mock);
        user.setAccounts(list);

        when(mock.getID()).thenReturn("57");


        assertEquals("57", user.getAccounts().get(0).getID());
    }
}
