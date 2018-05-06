package com.myhalf.model.entities;

import java.util.ArrayList;
import java.util.List;


public class UserMatch extends User {
    List<UserSeeker> suggestions = new ArrayList<>();
    AboutMeMatch aboutMe;

//-----------------------------------------methuds-----------------------------------------------------

//-----------------ctors-------------------------

    public UserMatch() {
        this.suggestions = suggestions;
        this.aboutMe = new AboutMeMatch();
    }



//-----------------getters&setters---------------

    public List<UserSeeker> getSuggestions() {
        return suggestions;
    }
    public void setSuggestions(List<UserSeeker> suggestions) {
        this.suggestions = suggestions;
    }

    public AboutMeMatch getAboutMe() {
        return aboutMe;
    }
    public void setAboutMe(AboutMe aboutMe) {
        this.aboutMe= (AboutMeMatch) aboutMe;
    }

}
