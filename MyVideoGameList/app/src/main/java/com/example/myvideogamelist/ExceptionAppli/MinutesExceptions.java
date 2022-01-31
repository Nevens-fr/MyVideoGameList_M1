package com.example.myvideogamelist.ExceptionAppli;

/**
 * Raise an exception when user give a wrong minutes time (>= 60, < 0)
 */
public class MinutesExceptions extends Exception{
    public MinutesExceptions(){
        super("Minutes exception raised");
    }
}
