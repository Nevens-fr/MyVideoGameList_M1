package com.example.myvideogamelist.InterfacesAppli;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

public interface Fragmentable {
    void setFragmentActivity(AppCompatActivity fra);
    void setType(String type);
    String getType();
    void build();
}
