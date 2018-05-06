package com.myhalf.model.entities;

import java.io.Serializable;
import java.sql.Date;
import java.util.Calendar;

public class DateBuilt implements Serializable {
    private int year = 0;
    private int month = 0;
    private int day = 0;

    //----------- C-tors --------------
    public DateBuilt() {
        year = 0;
        month = 0;
        day = 0;
    }

    public DateBuilt(DateBuilt dateBuilt) {
        this.year = dateBuilt.year;
        this.month = dateBuilt.month;
        this.day = dateBuilt.day;
    }

    public DateBuilt(int year, int month, int day) {
        String date = Tools.clarifyDateString(day +"/"+month+"/"+year) ;
        String[] splittedDate= date.split("/");
        this.year = Integer.valueOf(splittedDate[2]);
        this.month = Integer.valueOf(splittedDate[1]);
        this.day = Integer.valueOf(splittedDate[0]);
    }

    public DateBuilt(String string) {
        string = Tools.clarifyDateString(string);
        String d_m_y[] = string.split("/");
        year = Integer.parseInt(d_m_y[2]);
        month = Integer.parseInt(d_m_y[1]);
        day = Integer.parseInt(d_m_y[0]);
    }

    // --------- calculate age by birthDate ----------
    public int findAge() {
        Calendar currentDate = Calendar.getInstance();
        int currentYear = currentDate.get(Calendar.YEAR);
        int birthYear = this.getYear();
        int age = currentYear - birthYear;

        int CurrentMonth = currentDate.get(Calendar.MONTH) + 1;
        int birthMonth = this.getMonth();
        if (birthMonth > CurrentMonth) {
            age--;
        } else if (birthMonth == CurrentMonth) {
            int currentDay = currentDate.get(Calendar.DAY_OF_MONTH);
            int birthDay = this.getDay();
            if (birthDay > currentDay) {
                age--;

            }
        }
        if (age > 0)
        return age;
        return 0;
    }


    @Override
    public String toString() {
        String string = day + "/" + month + "/" + year;
        string = Tools.clarifyDateString(string);
        return string;
    }

    public static class Tools {
        private static final String TAG = "tagDateFormatting";

        public static boolean isParsable(String input) {
            boolean parsable = true;
            try {
                Integer.parseInt(input);
            } catch (NumberFormatException e) {
                parsable = false;
            }
            return parsable;
        }

        public static String clarifyDateString(String dateB) {
            //, make any available date string look like : "dd/mm/yyyy" (throws exceptions)
            if (dateB == "0/0/0"||dateB==null)
                return "0/0/0";
            int days = 1, months = 1, years = Calendar.getInstance().getTime().getYear() - 70;
            String daysString = "", monthsString = "", yearsString = "";
            String[] d_m_y = dateB.split("/");
            try {
                String exceptionString = "Not available Date Format";
                if (d_m_y.length > 3 || d_m_y.length < 2)
                    throw new Exception(exceptionString + ", Cause: format problem.");
                else {
                    if (d_m_y[0].length() > 2 || d_m_y[0].length() < 1) {
                        throw new Exception(exceptionString + ", Cause: Days too many lyrics.");
                    }
                    if (d_m_y[1].length() > 2 || d_m_y[1].length() < 1) {
                        throw new Exception(exceptionString + ", Cause: Months too many lyrics.");
                    }
                    if (d_m_y.length == 3) {
                        if (d_m_y[2].length() != 4 && d_m_y[2].length() != 2 && d_m_y[2].length() != 1) {
                            throw new Exception(exceptionString + ", Cause: Years incorrect amount of lyrics.");
                        }
                    }
                }
                if (isParsable(d_m_y[0]) && isDaysCorrect(d_m_y[0])) {
                    days = Integer.parseInt(d_m_y[0]);
                } else {
                    throw new Exception(exceptionString + ", Cause: Days Input incorrect");
                }

                if (isParsable(d_m_y[1]) && isMonthsCorrect(d_m_y[1])) {
                    months = Integer.parseInt(d_m_y[1]);
                } else {
                    throw new Exception(exceptionString + ", Cause: Months Input incorrect");
                }

                if (d_m_y.length == 3 &&
                        isParsable(d_m_y[2]) &&
                        isYearsCorrect(d_m_y[2])
                        ) {
                    years = Integer.parseInt(d_m_y[2]);
                } else if (d_m_y.length == 3) {
                    throw new Exception(exceptionString + ", Cause: Years Input incorrect");
                }
            } catch (Exception ex) {
                StackTraceElement[] stack = ex.getStackTrace();

                String str = "";
                str += "<-------------------------------------------------------->\n";
                for (StackTraceElement item : stack) {
                    str += "Source: " + item;
                    str += "\n";
                    str += "\t Class: " + item.getClassName() + "\n";
                    str += "\t Method: " + item.getMethodName() + "\n";
                    str += "\t Line: " + item.getLineNumber() + "\n";
                }
                str += "\n<---------------------------------------------------->\n";
                str += "Message: " + ex.getMessage() + "\n";
//                Log.d(TAG, str);
                System.out.println(str);
                return null;
            }
            if (days < 10) {
                daysString = "0" + String.valueOf(days);
            } else {
                daysString = String.valueOf(days);
            }
            if (months < 10) {
                monthsString = "0" + String.valueOf(months);
            } else {
                monthsString = String.valueOf(months);
            }

            dateB = daysString + "/" + monthsString;
            if (d_m_y.length == 3) {
                int year = Calendar.getInstance().getTime().getYear();
                if (years > 100)
                    yearsString = String.valueOf(years);
                else if (years < 10)
                    yearsString = "200" + String.valueOf(years);
                else if (years < year - 100)
                    yearsString = "20" + String.valueOf(years);
                else
                    yearsString = "19" + String.valueOf(years);
                dateB += "/" + yearsString;
            } else
                dateB += "/" + currentYear();
            return dateB;
        }

        private static String currentYear() {
            return String.valueOf(Calendar.getInstance().getTime().getYear() + 1900);
        }

        private static boolean isYearsCorrect(String s) {
            return Integer.parseInt(s) <= Calendar.getInstance().getTime().getYear() + 1900
                    && Integer.parseInt(s) >= Calendar.getInstance().getTime().getYear() + 1900 - 70
                    ||
                    Integer.parseInt(s) <= 100 && Integer.parseInt(s) >= 0;
        }

        private static boolean isMonthsCorrect(String s) {
            return Integer.parseInt(s) <= 12 && Integer.parseInt(s) >= 1;
        }

        private static boolean isDaysCorrect(String s) {
            return Integer.parseInt(s) <= 31 && Integer.parseInt(s) >= 1;
        }
    }


    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }
}