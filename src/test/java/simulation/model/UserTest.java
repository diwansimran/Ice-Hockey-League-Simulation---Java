package simulation.model;

import db.data.ILeagueDao;
import db.data.IUserDao;
import org.junit.BeforeClass;
import org.junit.Test;
import simulation.mock.LeagueMock;
import simulation.mock.UserMock;

import static org.junit.Assert.*;

public class UserTest {

    private static IUserDao loadUserFactory;

    @BeforeClass
    public static void setFactoryObj() {
        loadUserFactory = new UserMock();
    }

    @Test
    public void defaultConstructorTest() {
        User user = new User();
        assertNotEquals(user.getId(), 0);
    }

    @Test
    public void userTest() {
        User user = new User(1);
        assertEquals(user.getId(), 1);
    }

    @Test
    public void userFactoryTest() throws Exception {
        User user = new User(1, loadUserFactory);
        assertEquals(user.getId(), 1);
        assertEquals(user.getName(), "User1");

        user = new User(2, loadUserFactory);
        assertNull(user.getName());
    }

    @Test
    public void getPasswordTest() throws Exception {
        User user = new User(1, loadUserFactory);
        assertEquals(user.getPassword(), ("Password1"));
    }

    @Test
    public void setPasswordTest() {
        User user = new User();
        String password = "Password";
        user.setPassword(password);
        assertEquals(user.getPassword(), (password));
    }

    @Test
    public void getLeagueTest() throws Exception {
        User user = new User(1, loadUserFactory);
        ILeague league = user.getLeagueList().get(0);
        assertEquals(league.getId(), (1));
    }

    @Test
    public void setLeagueTest() throws Exception {
        User user = new User(1, loadUserFactory);
        ILeagueDao leagueFactory = new LeagueMock();
        League league = new League(1, leagueFactory);
        assertEquals(user.getLeagueList().get(0).getId(), league.getId());
    }

    @Test
    public void getLeagueListTest() throws Exception {
        User user = new User(1, loadUserFactory);
        ILeague league = user.getLeagueList().get(0);
        assertEquals(league.getId(), (1));
    }

    @Test
    public void setLeagueListTest() throws Exception {
        User user = new User(1, loadUserFactory);
        ILeagueDao leagueFactory = new LeagueMock();
        League league = new League(1, leagueFactory);
        assertEquals(user.getLeagueList().get(0).getId(), league.getId());
    }

    @Test
    public void addUserTest() throws Exception {
        User user = new User();
        user.setId(1);
        user.setName("User1");
        user.addUser(loadUserFactory);
        assertEquals(1, user.getId());
        assertEquals("User1", (user.getName()));
    }


}
