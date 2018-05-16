package com.myhalf.controller.navigation;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.SearchView;

import com.myhalf.R;
import com.myhalf.controller.MyUser;
import com.myhalf.model.backend.DBManager;
import com.myhalf.model.backend.DBManagerFactory;
import com.myhalf.model.backend.Finals;
import com.myhalf.model.entities.UserSeeker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class SearchResults extends Fragment {

    DBManager DB_users = DBManagerFactory.getSeekerManager();
    UserSeeker activityUser = MyUser.getUserSeeker();
    List<UserSeeker> allUsers = new ArrayList<>();


    View v = getView();
    SharedPreferences sharedPreferences;
    AdapterRecycleView adapterRecycleView;

    private RecyclerView recyclerView;
    SearchView svFilter;
    List<UserSeeker> filteredList = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search_results, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        findViews();

        svFilter.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (adapterRecycleView != null&&newText!=null) {
                adapterRecycleView.getFilter().filter(newText);
                adapterRecycleView.notifyDataSetChanged();
                }
                return true;
            }
        });

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());// for using it on later use
        new GetAllAsync().execute();

    }

    private void findViews() {
        View v = getView();
        svFilter = (SearchView) v.findViewById(R.id.svFilter);
        recyclerView = (RecyclerView) v.findViewById(R.id.recycleView);

    }

    private boolean matchToSearch(UserSeeker element) {
        if (element!=null)
            return true;

        int fromAge = sharedPreferences.getInt(Finals.DB.FROM_AGE, 0);
        int toAge = sharedPreferences.getInt(Finals.DB.TO_AGE, 100);
        int fromHeight = sharedPreferences.getInt(Finals.DB.FROM_HEIGHT, 0);
        int toHeight = sharedPreferences.getInt(Finals.DB.TO_HEIGHT, 220);
        String status = null;
        if (sharedPreferences.contains(Finals.DB.AboutMe.STATUS))
            status = sharedPreferences.getString(Finals.DB.AboutMe.STATUS, null);
        String eda = null;
        if (sharedPreferences.contains(Finals.DB.AboutMe.EDA))
            eda = sharedPreferences.getString(Finals.DB.AboutMe.EDA, null);
        String[] views = null;
        if (sharedPreferences.contains(Finals.DB.AboutMe.VIEW))
            views = setToStringArray(sharedPreferences.getStringSet(Finals.DB.AboutMe.VIEW, null));
        String[] hairCoverWomen = null;
        //------------ check if element match to search details --------------

        if (element.getAboutMe().getBirthday().findAge() >= fromAge &&
                element.getAboutMe().getBirthday().findAge() <= toAge &&
                (element.getAboutMe().getHeight() >= fromHeight || element.getAboutMe().getHeight() == 0) &&
                (element.getAboutMe().getHeight() <= toHeight || element.getAboutMe().getHeight() == 0) &&
                (status == null ||element.getAboutMe().getStatus()==null|| element.getAboutMe().getStatus().equalsIgnoreCase(status)) &&
                (eda == null ||element.getAboutMe().getWitness()==null|| containsCaseInsensitive(element.getAboutMe().getWitness(), eda)) &&
                stringContaints(element.getAboutMe().getView(), views))

            return true;

        return false;
    }
    public static boolean containsCaseInsensitive(List<String> arrayString, String s){
        for (String string : arrayString){
            if (string.equalsIgnoreCase(s)){
                return true;
            }
        }
        return false;
    }


    public static boolean stringContaints(List<String> stringArraycontains, String[] stringArrayIncluded) {
        if (stringArrayIncluded == null)
            return true;
        if (stringArraycontains == null)
            return false;

        List<String> includes = Arrays.asList(stringArrayIncluded);
        for (String item : includes) {
            if (containsCaseInsensitive(stringArraycontains,item))
                return true;
        }
        return false;
    }

    private String[] setToStringArray(Set<String> stringSet) {
        if (stringSet == null)
            return null;
        return stringSet.toArray(new String[stringSet.size()]);
    }

    public class GetAllAsync extends AsyncTask<Void, ProgressBar,List<UserSeeker>>{


        @Override
        protected List<UserSeeker> doInBackground(Void... voids) {
            List<UserSeeker> userSeekers = (ArrayList<UserSeeker>) DB_users.getUsersList();
            List<UserSeeker> filteredUsers= new ArrayList<>();
            if (userSeekers != null)
                for (Iterator<UserSeeker> iter = userSeekers.iterator(); iter.hasNext(); ) {
                    UserSeeker element = iter.next();
                    if (matchToSearch(element))
                        filteredUsers.add(element);
                }
            return filteredUsers;
        }
        @Override
        protected void onPostExecute(List<UserSeeker> userSeekers) {
            super.onPostExecute(userSeekers);
            filteredList = userSeekers;
            adapterRecycleView = new AdapterRecycleView(filteredList, getActivity());
            recyclerView.setAdapter(adapterRecycleView);
        }

        @Override
        protected void onProgressUpdate(ProgressBar... values) {
            super.onProgressUpdate(values);
        }
    }

}



