package com.example.myvideogamelist.InterfacesAppli;

public interface ObservableAppli {
    void subscribe(ObservatorAppli obs);
    void notifyObs(int status);
    void unsubscribe(ObservatorAppli obs);
}
