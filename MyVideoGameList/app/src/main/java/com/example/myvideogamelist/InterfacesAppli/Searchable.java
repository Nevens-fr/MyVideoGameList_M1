package com.example.myvideogamelist.InterfacesAppli;

import org.json.JSONObject;

public interface Searchable {

    void clearViewsAndWaitSearch();
    int getPageNumber();
    void setPageNumber(int pnb);
    void scrollUp();
    String getHint();
    void setHint(String hint);
    void networkError();
    void removeErrorAndLoading();
    void setLoading();
    void createCards(JSONObject obj, int actualElement);
}
