package com.example.myvideogamelist;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

import com.example.myvideogamelist.Notification.DateNotif;

public class DateNotifUnitTest {

    @Test
    public void testCompareEquals(){
        DateNotif dn = new DateNotif(2020, 10, 12);
        DateNotif dn2 = new DateNotif(2020, 10, 12);
        assertEquals(dn.compareTo(dn2), 0);
    }

    @Test
    public void testCompareNEquals1(){
        DateNotif dn = new DateNotif(2020, 10, 12);
        DateNotif dn2 = new DateNotif(2020, 10, 1);
        assertEquals(dn.compareTo(dn2), 1);
    }

    @Test
    public void testCompareNEquals2(){
        DateNotif dn = new DateNotif(2020, 1, 12);
        DateNotif dn2 = new DateNotif(2020, 10, 1);
        assertEquals(dn.compareTo(dn2), -1);
    }

    @Test
    public void testCompareNEquals3(){
        DateNotif dn = new DateNotif(2021, 10, 12);
        DateNotif dn2 = new DateNotif(2020, 10, 1);
        assertEquals(dn.compareTo(dn2), 1);
    }
}
