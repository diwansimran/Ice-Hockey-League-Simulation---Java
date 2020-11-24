package simulation.model;

import simulation.dao.ILeagueDao;
import simulation.dao.IPlayerDao;
import org.junit.BeforeClass;
import org.junit.Test;
import simulation.mock.LeagueMock;
import simulation.mock.PlayerMock;

import java.time.LocalDate;

import static org.junit.Assert.*;

public class PlayerTest {

    private static IPlayerDao loadPlayerFactory;

    @BeforeClass
    public static void setFactoryObj() {
        loadPlayerFactory = new PlayerMock();
    }

    @Test
    public void defaultConstructorTest() {
        IPlayer player = new Player();
        assertNotEquals(player.getId(), 0);
    }

    @Test
    public void playerTest() {
        IPlayer player = new Player(1);
        assertEquals(player.getId(), 1);
    }

    @Test
    public void playerNullTest() {
        IPlayer player = new Player(null);
        assertEquals(player.getId(), 0);
    }

    @Test
    public void playerFactoryTest() throws Exception {
        IPlayer player = new Player(1, loadPlayerFactory);
        assertEquals(player.getId(), 1);
        assertEquals(player.getName(), "Player1");

        player = new Player(33, loadPlayerFactory);
        assertNull(player.getName());
    }

    @Test
    public void getAgeTest() throws Exception {
        IPlayer player = new Player(1, loadPlayerFactory);
        assertEquals(player.getAge(), 27);
    }

    @Test
    public void setAgeTest() {
        IPlayer player = new Player();
        int age = 15;
        player.setAge(age);
        assertEquals(player.getAge(), age);
    }

    @Test
    public void getPositionTest() throws Exception {
        IPlayer player = new Player(1, loadPlayerFactory);
        assertEquals(player.getPosition(), Position.FORWARD);
    }

    @Test
    public void setPositionTest() {
        IPlayer player = new Player();
        Position position = Position.GOALIE;
        player.setPosition(position);
        assertEquals(player.getPosition(), position);
    }

    @Test
    public void getTeamIdTest() throws Exception {
        IPlayer player = new Player(1, loadPlayerFactory);
        assertEquals(player.getTeamId(), (1));
    }

    @Test
    public void setTeamIdTest() {
        IPlayer player = new Player();
        int teamId = 1;
        player.setTeamId(teamId);
        assertEquals(player.getTeamId(), teamId);
    }

    @Test
    public void isCaptainTest() throws Exception {
        IPlayer player = new Player(1, loadPlayerFactory);
        assertTrue(player.isCaptain());
    }

    @Test
    public void setCaptainTest() {
        IPlayer player = new Player();
        boolean isCaptain = true;
        player.setCaptain(true);
        assertTrue(player.isCaptain());
    }

    @Test
    public void addPlayerTest() throws Exception {
        IPlayer player = new Player();
        player.setId(1);
        player.setName("Player1");
        player.addPlayer(loadPlayerFactory);
        assertEquals(1, player.getId());
        assertEquals("Player1", player.getName());
    }

    @Test
    public void getFreeAgentIdTest() throws Exception {
        IPlayer player = new Player(1, loadPlayerFactory);
        assertEquals(player.getFreeAgentId(), (1));
    }

    @Test
    public void setFreeAgentIdTest() {
        IPlayer player = new Player();
        int freeAgentId = 1;
        player.setFreeAgentId(freeAgentId);
        assertEquals(player.getFreeAgentId(), freeAgentId);
    }

    @Test
    public void getInjuredTest() throws Exception {
        IPlayer player = new Player(1, loadPlayerFactory);
        assertTrue(player.getInjured());
    }

    @Test
    public void getInjuredFalseTest() throws Exception {
        IPlayer player = new Player(2, loadPlayerFactory);
        assertFalse(player.getInjured());
    }

    @Test
    public void setInjuredTest() {
        IPlayer player = new Player();
        player.setInjured(false);
        assertFalse(player.getInjured());
    }

    @Test
    public void getInjuryStartDateTest() throws Exception {
        IPlayer player = new Player(1, loadPlayerFactory);
        assertEquals(player.getInjuryStartDate(), LocalDate.now());
    }

    @Test
    public void setInjuryStartDateTest() {
        IPlayer player = new Player();
        player.setInjuryStartDate(LocalDate.now());
        assertEquals(player.getInjuryStartDate(), LocalDate.now());
    }

    @Test
    public void getInjuryDatesRangeTest() throws Exception {
        IPlayer player = new Player(1, loadPlayerFactory);
        assertEquals(player.getInjuryDatesRange(), 80);
    }

    @Test
    public void setInjuryDatesRangeTest() {
        IPlayer player = new Player();
        player.setInjuryDatesRange(100);
        assertEquals(player.getInjuryDatesRange(), 100);
    }

    @Test(expected = IllegalArgumentException.class)
    public void throwsExceptionWhenAgeIsNegativeNumber() {
        IPlayer player = new Player();
        player.setAge(-20);
    }

    @Test(expected = IllegalArgumentException.class)
    public void throwsExceptionWhenSkatingIsOutOfRange() {
        IPlayer player = new Player();
        player.setSkating(0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void throwsExceptionWhenShootingIsOutOfRange() {
        IPlayer player = new Player();
        player.setShooting(22);
    }

    @Test(expected = IllegalArgumentException.class)
    public void throwsExceptionWhenCheckingIsOutOfRange() {
        IPlayer player = new Player();
        player.setChecking(78);
    }

    @Test(expected = IllegalArgumentException.class)
    public void throwsExceptionWhenSavingIsOutOfRange() {
        IPlayer player = new Player();
        player.setSaving(0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setInvalidPositionTest() throws Exception {
        IPlayer player = new Player(32, loadPlayerFactory);
    }

    @Test
    public void retirementCheckTest() throws Exception {
        ILeagueDao leagueFactory = new LeagueMock();
        ILeague league = new League(1, leagueFactory);
        Player player = new Player(20, loadPlayerFactory);
        boolean retired = player.retirementCheck(league);
        assertTrue(retired);
    }

    @Test
    public void isRetiredTest() throws Exception {
        Player player = new Player(1, loadPlayerFactory);
        assertFalse(player.isRetired());
    }


    @Test
    public void calculateAgeTest() throws Exception {
        ILeagueDao leagueFactory = new LeagueMock();
        ILeague league = new League(1, leagueFactory);
        Player player = new Player(31, loadPlayerFactory);
        player.calculateAge(league);
        assertEquals(player.getAge(), 23);
    }

    @Test
    public void injuryCheckTest() throws Exception {
        ILeagueDao leagueFactory = new LeagueMock();
        ILeague league = new League(1, leagueFactory);
        IPlayer player = new Player(2, loadPlayerFactory);
        assertFalse(player.getInjured());
        player.injuryCheck(league);
        assertTrue(player.getInjured());
    }

    @Test
    public void injuryCheckInjuredTest() throws Exception {
        ILeagueDao leagueFactory = new LeagueMock();
        ILeague league = new League(1, leagueFactory);
        IPlayer player = new Player(1, loadPlayerFactory);
        player.injuryCheck(league);
        assertTrue(player.getInjured());
    }

    @Test
    public void injuryCheckNUllTest() throws Exception {
        IPlayer player = new Player(2, loadPlayerFactory);
        player.injuryCheck(null);
        assertFalse(player.getInjured());
    }

    @Test
    public void agingInjuryRecoveryTest() throws Exception {
        IPlayer player = new Player(12, loadPlayerFactory);
        ILeagueDao leagueFactory = new LeagueMock();
        ILeague league = new League(1, leagueFactory);
        assertTrue(player.getInjured());
        assertNotNull(player.getInjuryStartDate());
        assertEquals(player.getInjuryDatesRange(), 80);
        player.agingInjuryRecovery(league);
        assertFalse(player.getInjured());
        assertNull(player.getInjuryStartDate());
        assertEquals(player.getInjuryDatesRange(), 0);
    }

    @Test
    public void agingInjuryRecoveryNullTest() throws Exception {
        IPlayer player = new Player(12, loadPlayerFactory);
        player.agingInjuryRecovery(null);
        assertTrue(player.getInjured());
        assertNotNull(player.getInjuryStartDate());
        assertEquals(player.getInjuryDatesRange(), 80);
    }
}
