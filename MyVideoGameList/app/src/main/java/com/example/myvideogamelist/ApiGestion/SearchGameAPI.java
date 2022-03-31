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

    /**
     * setter of the developers
     * @param developpers the developers to set
     */
    public void setDevelopers(String developpers) { this.developers = developpers; }

    /**
     * setter of the publishers
     * @param publishers the publishers to set
     */
    public void setPublishers(String publishers) { this.publishers = publishers; }

    /**
     * setter of the page
     * @param page the page to set
     */
    public void setPage(String page) {
        this.page = page;
    }

    /**
     * setter of the page_size
     * @param page_size the page_size to set
     */
    public void setPage_size(String page_size) {
        this.page_size = page_size;
    }

    /**
     * setter of the search
     * @param search the search to set
     */
    public void setSearch(String search) {
        this.search = search;
    }

    /**
     * setter of the platforms
     * @param platforms the platforms to set
     */
    public void setPlatforms(String platforms) {
        this.platforms = platforms;
    }

    /**
     * setter of the genres
     * @param genres the genres to set
     */
    public void setGenres(String genres) {
        this.genres = genres;
    }

    /**
     * setter of the dates
     * @param dates the dates to set
     */
    public void setDates(String dates) {
        this.dates = dates;
    }

    /**
     * setter of the ordering type
     * @param type the ordering type to set
     */
    public void setOrdering(String type) { ordering = type; }

    /**
     * Return parameters for the search
     * @return string from all fields
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
