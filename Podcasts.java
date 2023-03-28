package Operations;

import java.util.Date;

public class Podcasts {
    private int podcastsID;
    private String podcastsName;
    private String celebrityName;
    private Date publishedDate;
    private int episodeNum;

    //getter and setter

    public Podcasts(int podcastsID, String podcastsName, String celebrityName, Date publishedDate, int episodeNum) {
        this.podcastsID = podcastsID;
        this.podcastsName = podcastsName;
        this.celebrityName = celebrityName;
        this.publishedDate = publishedDate;
        this.episodeNum = episodeNum;
    }

    public int getPodcastsID() {
        return podcastsID;
    }

    public void setPodcastsID(int podcastsID) {
        this.podcastsID = podcastsID;
    }

    public String getPodcastsName() {
        return podcastsName;
    }

    public void setPodcastsName(String podcastsName) {
        this.podcastsName = podcastsName;
    }

    public String getCelebrityName() {
        return celebrityName;
    }

    public void setCelebrityName(String celebrityName) {
        this.celebrityName = celebrityName;
    }

    public Date getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(Date publishedDate) {
        this.publishedDate = publishedDate;
    }

    public int getEpisodeNum() {
        return episodeNum;
    }

    //constructor

    public void setEpisodeNum(int episodeNum) {
        this.episodeNum = episodeNum;
    }


    //to string method

    @Override
    public String toString() {
        return "Operations.Podcasts{" + "podcastsID=" + podcastsID + ", podcastsName='" + podcastsName + '\'' + ", celebrityName='" + celebrityName + '\'' + ", publishedDate=" + publishedDate + ", episodeNum=" + episodeNum + '}';
    }
}

