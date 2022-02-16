package com.example.myvideogamelist;

import static org.junit.Assert.assertEquals;

import com.example.myvideogamelist.Security.PasswordProtect;

import org.junit.Test;

public class PasswordProtectUnitTest {

    @Test
    public void testSHA_512(){
        assertEquals(PasswordProtect.hashPassword("testDeMdp"), PasswordProtect.hashPassword("testDeMdp"));
    }

    @Test
    public void testCompare(){
        assertEquals(true, PasswordProtect.comparePassword("myPassword", PasswordProtect.hashPassword("myPassword")));
    }
}
