package com.example.myvideogamelist.Notification;

/**
 * Class used to compare current date and a game date
 */
public class DateNotif {
    private int year, month, day;

    /**
     * Constructor
     * @param year year
     * @param month month
     * @param day day
     */
    public DateNotif(int year, int month, int day){
        this.year = year;
        this.month = month;
        this.day = day;
    }

    /**
     * Compare two dates
     * @param d2 another date
     * @return <0 if this is more recent, 0 if equals, > 0 if d2 is more recent
     */
    public int compareTo(DateNotif d2){
        if(d2.getYear() == this.year){
            if(d2.getMonth() == this.month){
                if(d2.getDay() == this.day){
                    return 0;
                }
                return (d2.getDay() < this.day ? 1 : -1);
            }
            return (d2.getMonth() < this.month ? 1 : -1);
        }
        return (d2.getYear() < this.year ? 1 : -1);
    }

    /**
     * Getter of date
     * @return year 
     */
    public int getYear() {
        return year;
    }

    /**
     * Setter of date
     * @param year the year to set
     */
    public void setYear(int year) {
        this.year = year;
    }

    /**
     * Getter of month
     * @return the month to get
     */
    public int getMonth() {
        return month;
    }

    /** 
     * Setter of month
     * @param month the month to set 
     */
    public void setMonth(int month) {
        this.month = month;
    }

    /**
     * Getter of day
     * @return the day to get
     */
    public int getDay() {
        return day;
    }

    /**
     * Setter of day
     * @param day the day to set
     */
    public void setDay(int day) {
        this.day = day;
    }
}
