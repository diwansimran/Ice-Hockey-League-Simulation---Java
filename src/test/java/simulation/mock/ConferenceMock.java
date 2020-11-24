package simulation.mock;

import simulation.dao.IConferenceDao;
import simulation.dao.IDivisionDao;
import simulation.dao.ILeagueDao;
import simulation.factory.HockeyContextConcreteMock;
import simulation.factory.IConferenceFactory;
import simulation.factory.IDivisionFactory;
import simulation.factory.IHockeyContextFactory;
import simulation.model.IConference;
import simulation.model.IDivision;
import simulation.state.IHockeyContext;

import java.util.ArrayList;
import java.util.List;

public class ConferenceMock implements IConferenceDao {

    private IDivisionFactory divisionFactory;
    private IConferenceFactory conferenceFactory;
    private IDivisionDao divisionDao;
    private IConferenceDao conferenceDao;
    private IHockeyContextFactory hockeyContextFactory;
    private IHockeyContext hockeyContext;

    public ConferenceMock(){
        hockeyContextFactory = HockeyContextConcreteMock.getInstance();
        hockeyContext = hockeyContextFactory.newHockeyContext();
        divisionFactory = hockeyContext.getDivisionFactory();
        divisionDao = divisionFactory.newDivisionDao();
        conferenceFactory = hockeyContext.getConferenceFactory();
    }

    public List formDivisionList() throws Exception {
        List<IDivision> divisionList = new ArrayList<>();

        IDivision division = divisionFactory.newDivisionWithIdDao(1, divisionDao);
        divisionList.add(division);

        division = divisionFactory.newDivisionWithIdDao(2, divisionDao);
        divisionList.add(division);

        return divisionList;
    }

    public List formCreateTeamDivisionList() throws Exception {
        List<IDivision> divisionList = new ArrayList<>();

        IDivision division = divisionFactory.newDivisionWithIdDao(1, divisionDao);
        divisionList.add(division);

        division = divisionFactory.newDivisionWithIdDao(4, divisionDao);
        divisionList.add(division);

        return divisionList;
    }

    @Override
    public int addConference(IConference conference) throws Exception {
        conference = conferenceFactory.newConferenceWithId(1);
        return conference.getId();
    }

    @Override
    public void loadConferenceById(int id, IConference conference) throws Exception {

        switch (new Long(id).intValue()) {
            case 1:
                conference.setName("Conference1");
                conference.setLeagueId(1);
                conference.setDivisionList(formDivisionList());
                break;

            case 2:
                conference.setName(null);
                conference.setLeagueId(1);
                conference.setDivisionList(formDivisionList());
                break;

            case 3:
                conference.setName("Invalid Date");
                conference.setLeagueId(1);
                conference.setDivisionList(formDivisionList());
                break;

            case 4:
                conference.setName("Conference4");
                conference.setLeagueId(1);
                conference.setDivisionList(formCreateTeamDivisionList());
                break;
        }

    }

    @Override
    public IConference loadConferenceByName(String conferenceName) throws Exception {
        IConference conference = conferenceFactory.newConference();
        conference.setName("Conference1");
        conference.setLeagueId(1);
        conference.setDivisionList(formDivisionList());
        return conference;
    }

    @Override
    public List<IConference> loadConferenceListByLeagueId(int leagueId) throws Exception {
        ILeagueDao leagueDao = hockeyContext.getLeagueFactory().newLeagueDao();
        return leagueDao.formConferenceList();
    }
}
