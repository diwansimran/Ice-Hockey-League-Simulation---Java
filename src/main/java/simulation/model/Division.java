package simulation.model;

import db.data.IDivisionFactory;
import db.data.ITeamFactory;

import java.util.List;

public class Division extends ParentObj{

    public Division(){}

    public Division(int id){
        setId(id);
    }

    public Division(int id, IDivisionFactory factory) throws Exception {
        setId(id);
        factory.loadDivisionById(id, this);
    }

    private int conferenceId;

    private List<Team> teamList;

    public List<Team> getTeamList() {
        return teamList;
    }

    public void setTeamList(List<Team> teamList) {
        this.teamList = teamList;
    }

    public int getConferenceId() {
        return conferenceId;
    }

    public void setConferenceId(int conferenceId) {
        this.conferenceId = conferenceId;
    }

    public void addDivision(IDivisionFactory addDivisionFactory) throws Exception {
        addDivisionFactory.addDivision(this);
    }

    public void loadTeamListByDivisionId(ITeamFactory teamFactory) throws Exception {
        this.teamList = teamFactory.loadTeamListByDivisionId(getId());
    }
}