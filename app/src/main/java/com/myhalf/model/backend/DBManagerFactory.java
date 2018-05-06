package com.myhalf.model.backend;

import com.myhalf.model.datasource.Seeker_MySql;
import com.myhalf.model.datasource.Seeker_cFireStore;

public class DBManagerFactory {

    static DBManager seekerManager = null;

    static DBManager matchmakerManager = null;

    public static DBManager getSeekerManager(){
        if (seekerManager == null)
            seekerManager = new Seeker_cFireStore();
        return seekerManager;
    }

    public static  DBManager getMatchmakerManagerManager(){
        if (matchmakerManager == null)
            matchmakerManager = null;//new MatchmakerList();
        return matchmakerManager;
    }
}
