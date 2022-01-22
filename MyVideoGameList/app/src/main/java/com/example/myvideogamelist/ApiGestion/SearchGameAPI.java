package com.example.myvideogamelist.ApiGestion;

/**
 * Class to build a formatted parameters list
 */
public class SearchGameAPI {

    private String page, page_size, search, parent_platforms,
        platforms, stores, developers, publishers, genres, tags,
        dates, metacritic;

    /**
     * Constructor that put every parameter to null
     */
    public SearchGameAPI(){
        page = null;
        page_size = null;
        search = null;
        parent_platforms = null;
        platforms = null;
        stores = null;
        developers = null;
        publishers = null;
        genres = null;
        tags = null;
        dates = null;
        metacritic = null;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getPage_size() {
        return page_size;
    }

    public void setPage_size(String page_size) {
        this.page_size = page_size;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public String getParent_platforms() {
        return parent_platforms;
    }

    public void setParent_platforms(String parent_platforms) {
        this.parent_platforms = parent_platforms;
    }

    public String getPlatforms() {
        return platforms;
    }

    public void setPlatforms(String platforms) {
        this.platforms = platforms;
    }

    public String getStores() {
        return stores;
    }

    public void setStores(String stores) {
        this.stores = stores;
    }

    public String getDevelopers() {
        return developers;
    }

    public void setDevelopers(String developers) {
        this.developers = developers;
    }

    public String getPublishers() {
        return publishers;
    }

    public void setPublishers(String publishers) {
        this.publishers = publishers;
    }

    public String getGenres() {
        return genres;
    }

    public void setGenres(String genres) {
        this.genres = genres;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getDates() {
        return dates;
    }

    public void setDates(String dates) {
        this.dates = dates;
    }

    public String getMetacritic() {
        return metacritic;
    }

    public void setMetacritic(String metacritic) {
        this.metacritic = metacritic;
    }

    /**
     * Return parameters for the search
     * @return
     */
    @Override
    public String toString(){
        String build = "";

        build += page == null ? "":"&page="+page;
        build += page_size == null ? "":"&page_size="+page_size;
        build += search == null ? "":"&search="+search;
        build += parent_platforms == null ? "":"&parent_platforms="+parent_platforms;
        build += platforms == null ? "":"&platforms="+platforms;
        build += stores == null ? "":"&stores="+stores;
        build += developers == null ? "":"&developers="+developers;
        build += genres == null ? "":"&genres="+genres;
        build += tags == null ? "":"&tags="+tags;
        build += dates == null ? "":"&dates="+dates;
        build += metacritic == null ? "":"&metacritic="+metacritic;

        return build;
    }
}
