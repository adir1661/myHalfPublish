package com.myhalf.model.backend;

public class Finals {
    public static class DB{

        //------------------------------defaults---------------------------------------
        public static final String TO_AGE = "toAge";
        public static final String FROM_AGE = "fromAge";
        public static final String TO_HEIGHT = "toHeight";
        public static final String FROM_HEIGHT = "fromHeight";

        //------------------------------inner class User------------------------------
        public static final class User {
            public static final String ID = "_id";
            public static final String NAME = "name";
            public static final String PASSWORD = "password";
            public static final String EMAIL = "emailAdress";
            public static final String CELLPHONE = "cellphone";
            public static final String FIREBASE_TOKEN = "firebase_token";

        }

        //------------------------------inner class AboutMe------------------------------
        public static final class AboutMe {
            public static final String GENDER = "gender";
            public static final String BIRTHDAY = "birthday";
            public static final String HEIGHT = "height";
            public static final String VIEW = "view";
            public static final String WHAT_I_DO = "whatIDo";
            public static final String STATUS = "status";
            public static final String CITY = "city";
            public static final String EDA = "witness";
            public static final String LANGUAGES = "languages";
            public static final String FREE_DESCRIPTION = "free_description";
            public static final String ID = "user_id";
        }
    }
public static class App {
    public static final String USER_SEEKER = "userSeeker";
    public static final String JOB_UPDATE = "jobUpdate";
    public static final String PROFILE_PICTURE = "mainPicture";
    public static final String BUTTON_KEY = "button_in_edit_profile" ;
    public static final String SMALL_PICRTURE_1 = "smallButton1";
    public static final String SMALL_PICRTURE_2 = "smallButton2";
    public static final String SMALL_PICRTURE_3 = "smallButton3";
    public static final String SMALL_PICRTURE_4 = "smallButton4";
    public static final String SMALL_PICRTURE_5 = "smallButton5";
    public static final String OTHER_USER_CHAT = "chatWith";
}
public static class FireBase{
    public static final String LEGACY_SERVER_KEY ="AIzaSyAditM6-eNeeM7oSLW-YxUWHHFhkWGMZpA";
    public static final String FCM_PUSH_URL = "https://fcm.googleapis.com/fcm/send";
    public static class FirestoreCloud{
        public static final String MAIN_COLLECTION ="UsersSeekers";
        public static final String SEEKERS_DOC = "";
    }
    public static class storage{
        public static final String MAIN_PICTURE ="MainPicture";

        public static final String SMALL_PICTURE_1 = "SmallPicture1";
        public static final String SMALL_PICTURE_2 = "SmallPicture2";
        public static final String SMALL_PICTURE_3 = "SmallPicture3";
        public static final String SMALL_PICTURE_4 = "SmallPicture4";
        public static final String SMALL_PICTURE_5 = "SmallPicture5";

    }


    }

}
