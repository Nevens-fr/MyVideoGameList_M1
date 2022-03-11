package com.example.myvideogamelist.InterfacesAppli;

import androidx.fragment.app.FragmentActivity;

public interface Fragmentable {
    void setFragmentActivity(FragmentActivity fra);
    void setType(String type);
    String getType();
    void build();
}
