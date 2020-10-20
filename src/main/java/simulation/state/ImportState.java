package simulation.state;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import simulation.factory.*;
import simulation.model.*;
import java.util.ArrayList;
import java.util.List;

public class ImportState implements IHockeyState {

    private HockeyContext hockeyContext;
    private String filePath;
    private JSONObject jsonFromInput;
    private League league;



    public ImportState(HockeyContext hockeyContext,JSONObject jsonFromInput){
        this.jsonFromInput = jsonFromInput;
        this.hockeyContext = hockeyContext;
        league = hockeyContext.getUser().getLeague();
        if(league == null){
            LeagueConcrete leagueConcrete = new LeagueConcrete();
            league = leagueConcrete.newLeague();
        }
    }

    @Override
    public void entry() {
        //empty for now
    }

    @Override
    public void process() {
        parseJSONAndInstantiateLeague(jsonFromInput);
        hockeyContext.getUser().setLeague(league);
    }

    @Override
    public IHockeyState exit() {
        return null;
    }

    private void parseJSONAndInstantiateLeague(JSONObject leagueJSON){

        String leagueName = (String) leagueJSON.get("leagueName");
        JSONObject gameplayConfig = (JSONObject) leagueJSON.get("gameplayConfig");
        JSONArray conferences = (JSONArray) leagueJSON.get("conferences");
        JSONArray freeAgents = (JSONArray) leagueJSON.get("freeAgents");
        JSONArray coaches = (JSONArray) leagueJSON.get("coaches");
        JSONArray managers = (JSONArray) leagueJSON.get("generalManagers");


        if(validateString(leagueName) ){
            System.out.println("Please make sure league name is valid ");
            System.exit(1);
        }

        if(validateArray(conferences) ){
            System.out.println("Please make sure atleast one conference is provided ");
            System.exit(1);
        }
        if(validateArray(freeAgents) ){
            System.out.println("Please make sure atleast one Player in Free Agent is provided ");
            System.exit(1);
        }


        List<Conference> conferenceList = loadConferenceJSON(conferences);

        FreeAgentConcrete freeAgentConcrete = new FreeAgentConcrete();
        FreeAgent freeAgent = freeAgentConcrete.newFreeAgent();
        List<Player> freeAgentList = loadFreeAgentJSON(freeAgents);
        freeAgent.setPlayerList(freeAgentList);

        List<Coach> coachList = loadCoachJSON(coaches);
        List<Manager> managerList = loadManagerJSON(managers);

        league.setName(leagueName);
        league.setConferenceList(conferenceList);
        league.setFreeAgent(freeAgent);
        league.setCoachList(coachList);
        league.setManagerList(managerList);
    }
    /**
     *
     * Show list of free agents
     * Player choice capability
     *
     */

    private List<Team> loadTeamJSON(JSONArray teams){
        ArrayList<Team> teamList = new ArrayList<Team>();
        for(Object teamObjectFromJSONArray : teams){
            JSONObject teamJSONObject = (JSONObject) teamObjectFromJSONArray;
            String teamName = (String) teamJSONObject.get("teamName");
            String generalManager = (String) teamJSONObject.get("generalManager");
            String headCoach = (String) teamJSONObject.get("headCoach");

            if(validateString(teamName) ){
                System.out.println("Please make sure team name is valid ");
                System.exit(1);
            }

            if(validateString(generalManager) ){
                System.out.println("Please make sure General Manager name is valid ");
                System.exit(1);
            }
            if(validateString(headCoach) ){
                System.out.println("Please make sure Head Coach name is valid ");
                System.exit(1);
            }
            if(isTeamExists(teamList,teamName)){
                System.out.println("Please make sure there are no duplicates in conference name");
                System.exit(1);
            }


            TeamConcrete teamConcrete = new TeamConcrete();
            Team team = teamConcrete.newTeam();
            team.setName(teamName);
            team.setGeneralManager(generalManager);
            team.setHeadCoach(headCoach);

            JSONArray players = (JSONArray) teamJSONObject.get("players");

            if(validateArray(players) ){
                System.out.println("Please make sure atleast one player is provided. ");
                System.exit(1);
            }

            List<Player> playerList = loadPlayerJSON(players);

            team.setPlayerList(playerList);
            teamList.add(team);

        }
        return teamList;

    }
    private List<Player> loadPlayerJSON(JSONArray players){

        ArrayList<Player> playerList = new ArrayList<Player>();

        ArrayList<Boolean> captainList = new ArrayList<>();

        try {

            for (Object playerObjectFromJSONArray : players) {
                JSONObject playerJsonObject = (JSONObject) playerObjectFromJSONArray;
                String playerName = (String) playerJsonObject.get("playerName");
                String position = (String) playerJsonObject.get("position");
                boolean captain = (Boolean) playerJsonObject.get("captain");
                int age = (Integer) playerJsonObject.get("age");
                float skating = (Float) playerJsonObject.get("skating");
                float shooting = (Float) playerJsonObject.get("shooting");
                float checking = (Float) playerJsonObject.get("checking");
                float saving = (Float) playerJsonObject.get("saving");

                if (validateString(playerName)) {
                    System.out.println("Please make sure player name is valid ");
                    System.exit(1);
                }

                if (validateString(position)) {
                    System.out.println("Please make sure position of the player is valid");
                    System.exit(1);
                }

                if (validateBoolean(captain)) {
                    System.out.println("Please make sure captain is valid ");
                    System.exit(1);
                }

                if (validCaptain(playerList, captain)) {
                    System.out.println("Please make sure only one captain is provided ");
                    System.exit(1);
                }

                if (isPlayerExists(playerList, playerName)) {
                    System.out.println("Please make sure there are no duplicates in conference name");
                    System.exit(1);
                }

                captainList.add(captain);

                PlayerConcrete playerConcrete = new PlayerConcrete();
                Player player = playerConcrete.newPlayer();
                player.setName(playerName);
                player.setPosition(position);
                player.setCaptain(captain);
                player.setAge(age);
                player.setSkating(skating);
                player.setChecking(checking);
                player.setSaving(saving);

                if (player.validPosition() && player.validName()) {
                    playerList.add(player);
                } else {
                    System.out.println("Please make sure either of goalie, forward or defense are provided in the position. Nothing else!");
                    System.exit(1);
                }

            }
        }catch(ClassCastException e){
            System.out.println("Please make sure only boolean is provided for captain field.Exiting the app!");
            System.exit(1);
        }
        return playerList;

    }
    private List<Division> loadDivisionJSON(JSONArray divisions){
        ArrayList<Division> divisionList = new ArrayList<Division>();
        for(Object divisionObjectFromJSONArray : divisions){
            JSONObject divisionJSONObject = (JSONObject) divisionObjectFromJSONArray;
            String divisionName = (String) divisionJSONObject.get("divisionName");

            if(validateString(divisionName) ){
                System.out.println("Please make sure divisionName is valid");
                System.exit(1);
            }

            if(isDivisionExists(divisionList,divisionName)){
                System.out.println("Please make sure only one division is provided");
                System.exit(1);
            }

            Division division = new Division();
            division.setName(divisionName);

            JSONArray teams = (JSONArray) divisionJSONObject.get("teams");
            if(validateArray(teams) ){
                System.out.println("Please make sure atleast one team is provided");
                System.exit(1);
            }

            List<Team> teamList = loadTeamJSON(teams);

            division.setTeamList(teamList);
            divisionList.add(division);
        }
        return divisionList;
    }
    private List<Conference> loadConferenceJSON(JSONArray conferences){
        ArrayList<Conference> conferenceList = new ArrayList<Conference>();
        for(Object conferenceObjectFromJSONArray : conferences){
            JSONObject conferenceJSONObject = (JSONObject) conferenceObjectFromJSONArray;
            String conferenceName = (String) conferenceJSONObject.get("conferenceName");

            if(validateString(conferenceName) ){
                System.out.println("Please make sure conferenceName is valid ");
                System.exit(1);
            }

            if(isConferenceExists(conferenceList,conferenceName)){
                System.out.println("Please make sure there are no duplicates in conference name");
                System.exit(1);
            }


            ConferenceConcrete conferenceConcrete = new ConferenceConcrete();
            Conference conference = conferenceConcrete.newConference();

            conference.setName(conferenceName);

            JSONArray divisions = (JSONArray) conferenceJSONObject.get("divisions");

            if(validateArray(divisions) ){
                System.out.println("Please make sure atleast one division is provided ");
                System.exit(1);
            }

            List<Division> divisionList = loadDivisionJSON(divisions);

            conference.setDivisionList(divisionList);
            conferenceList.add(conference);
        }
        return conferenceList;
    }
    private List<Player> loadFreeAgentJSON(JSONArray freeAgents){

        ArrayList<Player> freeAgentList =  new ArrayList<>();

        try {

            for (Object freeAgentObjectFromJSONArray : freeAgents) {
                JSONObject freeAgentJsonObject = (JSONObject) freeAgentObjectFromJSONArray;
                String playerName = (String) freeAgentJsonObject.get("playerName");
                String position = (String) freeAgentJsonObject.get("position");

                if (validateString(playerName)) {
                    System.out.println("Please make sure player name is valid in Free Agent");
                    System.exit(1);
                }

                if (validateString(position)) {
                    System.out.println("Please make sure position of the player is valid in Free Agent");
                    System.exit(1);
                }

                if (isPlayerExists(freeAgentList, playerName)) {
                    System.out.println("Please make sure only  Player is unique in freeagent");
                    System.exit(1);
                }


                PlayerConcrete playerConcrete = new PlayerConcrete();
                Player player = playerConcrete.newPlayer();
                player.setName(playerName);
                player.setPosition(position);

                if (player.validPosition() && player.validName()) {
                    freeAgentList.add(player);
                } else {
                    System.out.println("Free Agent Position is not valid. Please Correct it. Exiting the app!");
                    System.exit(1);
                }

            }
        }catch(ClassCastException e){
            System.out.println("Please make sure only boolean is provided for captain field.Exiting the app!");
            System.exit(1);
        }
        return freeAgentList;
    }
    private List<Manager> loadManagerJSON(JSONArray managers) {
        ArrayList<Manager> managerList =  new ArrayList<>();
        int managerSize = managers.size();

        for (int i=0;i<managerSize;i++) {
            String name = (String) managers.get(i);
            ManagerConcrete managerConcrete = new ManagerConcrete();
            Manager manager = managerConcrete.newManagerConcrete();
            manager.setName(name);
        }
        return managerList;
    }

    private List<Coach> loadCoachJSON(JSONArray coaches) {
        ArrayList<Coach> coachList =  new ArrayList<>();
        for (Object coachObjectFromJSONArray : coaches) {
            JSONObject coachJsonObject = (JSONObject) coachObjectFromJSONArray;
            String name = (String) coachJsonObject.get("name");
            float skating = (Float) coachJsonObject.get("skating");
            float shooting = (Float) coachJsonObject.get("shooting");
            float checking = (Float) coachJsonObject.get("checking");
            float saving = (Float) coachJsonObject.get("saving");
            CoachConcrete coachConcrete = new CoachConcrete();
            Coach coach = coachConcrete.newCoach();
            coach.setName(name);
            coach.setSkating(skating);
            coach.setShooting(shooting);
            coach.setChecking(checking);
            coach.setSaving(saving);
            coachList.add(coach);
        }
        return coachList;
    }

    private boolean validateString(String name) {
        if (name != null && name.length() != 0) {
            return  false;
        } else {
            return true;
        }
    }
    private boolean validateArray(JSONArray array){
        if (array != null && array.size() != 0) {
            return  false;
        } else {
            return true;
        }
    }
    private boolean validateBoolean(Boolean bool){
        return bool == null;
    }

    private boolean validCaptain(List<Player> playerList,Boolean captain){
        boolean flag = false;
        for(Player vPlayer : playerList){
            if(captain && vPlayer.isCaptain()){
                flag =  true;
            }
        }
        return flag;
    }

    private boolean isDivisionExists(ArrayList<Division> list,String name){


        boolean flag = false;
        for(ParentObj obj : list){
            if(obj.getName().equals(name)){
                flag =  true;
            }
        }
        return flag;
    }
    private boolean isConferenceExists(ArrayList<Conference> list,String name){


        boolean flag = false;
        for(ParentObj obj : list){
            if(obj.getName().equals(name)){
                flag =  true;
            }
        }
        return flag;
    }
    private boolean isTeamExists(ArrayList<Team> list,String name){


        boolean flag = false;
        for(ParentObj obj : list){
            if(obj.getName().equals(name)){
                flag =  true;
            }
        }
        return flag;
    }
    private boolean isPlayerExists(ArrayList<Player> list,String name){


        boolean flag = false;
        for(ParentObj obj : list){
            if(obj.getName().equals(name)){
                flag =  true;
            }
        }
        return flag;
    }
}

