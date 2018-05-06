package com.myhalf.model.entities;

import android.app.Activity;

import com.myhalf.R;
import com.myhalf.model.backend.Finals;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class Enums implements Serializable {

    private Activity mActivity;
//    public final String AllOptions = mActivity.getString(R.string.AllOptions);


    //Gender
//    public final String MALE = mActivity.getString(R.string.MALE);
//    public final String FEMALE = mActivity.getString(R.string.FEMALE);
//    public String[] Min = {MALE,FEMALE};


    public static enum Gender {
        MALE, FEMALE, NULL;

        public static String[] getArraystring() {
            String[] str = Arrays.toString(Gender.values()).replaceAll("^.|.$", "").split(", ");
            return str;
        }


        //Status
//    public final String Single = mActivity.getString(R.string.Single);
//    public final String Divorced = mActivity.getString(R.string.divorced);
//    public final String Widow = mActivity.getString(R.string.widow);
//    public final String DivorcedWithChildren = mActivity.getString(R.string.DivorcedWithChildren);
//    public final String WidowWithChildren = mActivity.getString(R.string.WidowWithChildren);
//
//    public String Status[] = {AllOptions, Single, Divorced, Widow, DivorcedWithChildren, WidowWithChildren};


        //View
        public final String Masorty = "Traditional";
        public final String Religious = "Religious";
        public final String NationalReligious = "National Religious";
        public final String ReligiousNationalTorah = "Religious national Torah";
        public final String Orthodox = "Orthodox";
        public final String ModernOrthodox = "Modern Orthodox";
        public final String Breslev = "Breslev";
        public final String Chabad = "Chabad";
        public final String NewlyReligious = "newly religious";
        public final String LightReligious = "Light Religious";
        public final String Secular = "Secular";

        public String View[] = {Masorty, Religious, NationalReligious, ReligiousNationalTorah,
                Orthodox, ModernOrthodox, Breslev, Chabad, NewlyReligious, LightReligious, Secular};
    }
}


//    public static final String VIEW = Finals.DB.AboutMe.VIEW;
//    public static enum View {
//        DATI_LEUMI, DATI_TORANY, HILONI, HABADNUK, MASORTI, NULL;
//
//        public static String[] getArraystring() {
//            String[] str = Arrays.toString(View.values()).replaceAll("^.|.$", "").split(", ");
//            return removeNULL(str);
//        }
//    }





//    public static enum Eda {
//        ASHKENAZI, SFARADI, TEMANI, ETYOPI, RUSY, FRENCH, HYBRID , NULL;
//
//        public static String[] getArraystring() {
//            String[] str = Arrays.toString(Eda.values()).replaceAll("^.|.$", "").split(", ");
//            return removeNULL(str);
//        }
//
//    }







//    public static enum Status {
//        SINGLE, DIVORSED, ALMAN,NULL;//LO ALENU
//
//        public static String[] getArraystring() {
//            String[] str = Arrays.toString(Status.values()).replaceAll("^.|.$", "").split(", ");
//            return removeNULL(str);
//        }
//    }










