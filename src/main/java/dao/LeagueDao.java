package dao;

import common.Constants;
import data.ILeagueFactory;
import model.League;

import java.sql.ResultSet;
import java.sql.SQLException;

public class LeagueDao implements ILeagueFactory {

    @Override
    public int addLeague(League league) throws Exception{
        ICallDB callDB = null;
        try{
            callDB = new CallDB(Constants.addLeague);
            callDB.setInputParameterString(1, league.getName());
            callDB.setInputParameterInt(2, league.getCreatedBy());
            callDB.setOutputParameterInt(3);
            callDB.execute();
            league.setId(callDB.returnOutputParameterInt(3));

        } catch (SQLException sqlException){
            throw sqlException;
        } finally {
            callDB.closeConnection();
        }
        return league.getId();
    }

    @Override
    public void loadLeagueByName(int id, League league) throws Exception{
        ICallDB callDB = null;
        try {
            callDB = new CallDB(Constants.loadLeagueByName);
            callDB.setInputParameterString(1, league.getName());
            callDB.setOutputParameterInt(2);
            callDB.setOutputParameterString(3);
            callDB.setOutputParameterInt(4);
            ResultSet rs = callDB.executeLoad();
            if (rs != null) {
                while (rs.next()) {
                    league.setId(rs.getInt(2));
                    league.setName(rs.getString(3));
                    league.setCreatedBy(rs.getInt(4));
                }
            }
        }catch (Exception e){
            throw e;
        } finally {
            callDB.closeConnection();
        }
    }

    /*public static void main(String[] args){

        LeagueDao leagueDao = new LeagueDao();

        try {
            for (int i = 2; i < 10; i++) {
                League league = new League();
                league.setName("Name" + i);
                league.setCreatedBy(i);
                league.setId(leagueDao.addLeague(league));
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }*/

}
