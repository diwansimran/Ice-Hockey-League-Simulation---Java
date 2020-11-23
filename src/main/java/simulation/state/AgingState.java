package simulation.state;

import presentation.ConsoleOutput;
import simulation.model.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class AgingState implements ISimulateState {

    public static final String AGING_DAY = "Aging all players by one day!";
    private IHockeyContext hockeyContext;
    private ILeague league;

    public AgingState(IHockeyContext hockeyContext) {
        this.hockeyContext = hockeyContext;
        this.league = hockeyContext.getUser().getLeague();
    }

    @Override
    public ISimulateState action() {
        ConsoleOutput.getInstance().printMsgToConsole(AGING_DAY);
        agingPlayerDay(league);
        return exit();
    }

    private void agingPlayerDay(ILeague league) {
        List<IConference> conferenceList = league.getConferenceList();
        List<IPlayer> freeAgentList = league.getFreeAgent().getPlayerList();
        for (IConference conference : conferenceList) {
            List<IDivision> divisionList = conference.getDivisionList();
            for (IDivision division : divisionList) {
                List<ITeam> teamList = division.getTeamList();
                for (ITeam team : teamList) {
                    team.setActivePlayerList();
                    List<IPlayer> playerList = team.getPlayerList();
                    for (IPlayer teamPlayer : playerList) {
                        teamPlayer.calculateAge(league);
                        teamPlayer.statDecayCheck(league);
                        teamPlayer.agingInjuryRecovery(league);
                    }
                }
            }
        }
        for (IPlayer freeAgentPlayer : freeAgentList) {
            freeAgentPlayer.calculateAge(league);
            freeAgentPlayer.statDecayCheck(league);
            freeAgentPlayer.agingInjuryRecovery(league);
        }
    }

    private ISimulateState exit() {
        if (stanleyCupWinnerDetermined()) {
            updateTeamScoreList();
            return new AdvanceNextSeasonState(hockeyContext);
        } else {
            return new PersistState(hockeyContext);
        }
    }

    private void updateTeamScoreList() {
        HashMap<String,Integer> stanleyCupTeamStanding = league.getStanleyCupFinalsTeamScores();
        List<ITeamScore> teamScoreList = league.getActiveTeamStanding().getTeamsScoreList();
        for(ITeamScore teamScore : teamScoreList){
            stanleyCupTeamStanding.put(teamScore.getTeamName(),stanleyCupTeamStanding.get(teamScore.getTeamName()) + teamScore.getPoints());
        }
    }

    public Boolean stanleyCupWinnerDetermined() {
        INHLEvents nhlEvents = league.getNHLRegularSeasonEvents();
        IGameSchedule games = league.getGames();
        ITeamStanding teamStanding = league.getActiveTeamStanding();
        if (nhlEvents.checkRegularSeasonPassed(league.getCurrentDate())
                && games.doGamesDoesNotExistAfterDate(league.getCurrentDate()) && teamStanding.getTeamsScoreList().size() == 2) {
            return true;
        }
        return false;
    }
}
