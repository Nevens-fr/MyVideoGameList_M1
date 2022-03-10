package com.example.myvideogamelist.Notification;

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
