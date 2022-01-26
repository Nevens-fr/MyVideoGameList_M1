package com.example.myvideogamelist.ApiGestion;

public class Game {

    private String idGame, name, releasedDate, playtime, metacritic, description;
    private String []genres;
    private String []devs;
    private String []images;
    private String []publishers;

    /**
     * Return a stringified json
     * @return a stringified json
     */
    @Override
    public String toString(){
        String elem = "{\"id\":\""+idGame+"\",";
        elem += "\"name\":\""+name+"\",";
        elem += "\"released\":\""+releasedDate+"\",";
        elem += "\"playtime\":\""+playtime+"\",";
        elem += "\"metacritic\":\""+metacritic+"\",";
        elem += "\"description\":\""+description+"\",";

        elem += "\"genres\"" + returnTab(genres) +",";
        elem += "\"developers\"" + returnTab(devs) +",";
        elem += "\"short_screenshots\"" + returnTab(images) +",";
        elem += "\"publishers\"" + returnTab(publishers) + "}";

        return elem;
    }

    /**
     * Return an JSON array (in string) made from string array
     * @param arr string array to jsonify
     * @return a json array in string
     */
    private String returnTab(String []arr){
        String elem = ":[";

        for(String i : arr){
            elem += "{\"name\":\""+i+"\"},";
        }

        elem = elem.substring(0, elem.length() - 1);

        return elem + "]";
    }

    /**
     * Constructor to build arrays
     * @param nbGenres array size
     * @param nbDev array size
     * @param nbImages array size
     * @param nbPublishers array size
     */
    public Game(int nbGenres, int nbDev, int nbImages, int nbPublishers){
        genres = new String[nbGenres];
        images = new String[nbImages];
        devs = new String[nbDev];
        publishers = new String[nbPublishers];

    }

    public String getIdGame() {
        return idGame;
    }

    public void setIdGame(String idGame) {
        this.idGame = idGame;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReleasedDate() {
        return releasedDate;
    }

    public void setReleasedDate(String releasedDate) {
        this.releasedDate = releasedDate;
    }

    public String getPlaytime() {
        return playtime;
    }

    public void setPlaytime(String playtime) {
        this.playtime = playtime;
    }

    public String getMetacritic() {
        return metacritic;
    }

    public void setMetacritic(String metacritic) {
        this.metacritic = metacritic;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String[] getGenres() {
        return genres;
    }

    public void setGenres(String[] genres) {
        this.genres = genres;
    }

    public String[] getDevs() {
        return devs;
    }

    public void setDevs(String[] devs) {
        this.devs = devs;
    }

    public String[] getImages() {
        return images;
    }

    public void setImages(String[] images) {
        this.images = images;
    }

    public String[] getPublishers() {
        return publishers;
    }

    public void setPublishers(String[] publishers) {
        this.publishers = publishers;
    }
}
