package com.example.myvideogamelist.ApiGestion;

/**
 * Class to build a formatted parameters list
 */
public class SearchGameAPI {

    private String page;
    private String page_size;
    private String search;
    private String platforms;
    private String genres;
    private String dates;
    private String developers;
    private String publishers;
    private String ordering;

    /**
     * Constructor that put every parameter to null
     */
    public SearchGameAPI(){
        page = null;
        page_size = null;
        search = null;
        platforms = null;
        genres = null;
        dates = null;
        developers = null;
        publishers = null;
    }

    public void setDevelopers(String developpers) { this.developers = developpers; }

    public void setPublishers(String publishers) { this.publishers = publishers; }

    public void setPage(String page) {
        this.page = page;
    }

    public void setPage_size(String page_size) {
        this.page_size = page_size;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public void setPlatforms(String platforms) {
        this.platforms = platforms;
    }

    public void setGenres(String genres) {
        this.genres = genres;
    }

    public void setDates(String dates) {
        this.dates = dates;
    }

    public void setOrdering(String type) { ordering = type; }

    /**
     * Return parameters for the search
     * @return
     */
    @Override
    public String toString(){
        String build = "";

        build += search == null ? "":"&search="+search;
        build += page == null ? "":"&page="+page;
        build += developers == null ? "":"&developers="+developers;
        build += publishers == null ? "":"&publishers="+publishers;
        build += page_size == null ? "":"&page_size="+page_size;
        build += platforms == null ? "":"&platforms="+platforms;
        build += genres == null ? "":"&genres="+genres;
        build += dates == null ? "":"&dates="+dates;
        build += ordering == null ? "":"&ordering="+ordering;

        return build;
    }
}
