package com.example.myvideogamelist.MyExceptions;

/**
 * Class managing an error of research not returning anything
 */
public class EmptySearchException extends Exception{

    /**
    * Constructor of class
    */
    public EmptySearchException(){
        super("Empty search exception");
    }
}
