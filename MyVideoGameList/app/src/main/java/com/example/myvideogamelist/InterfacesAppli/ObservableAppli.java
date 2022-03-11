package com.example.myvideogamelist.InterfacesAppli;

/**
 * Observable of the application
 */
public interface ObservableAppli {
    void subscribe(ObservatorAppli obs);
    void notifyObs(int status);
    void unsubscribe(ObservatorAppli obs);
}
