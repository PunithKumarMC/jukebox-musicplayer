package Operations;

import java.sql.Time;

public class Song {
    private int songID;
    private String songName;
    private Time songDuration;
    private String artistName;
    private String genre;
    private String songAlbumName;
//getter and setter

    public Song(int songID, String songName, Time songDuration, String artistName, String genre, String songAlbumName) {
        this.songID = songID;
        this.songName = songName;
        this.songDuration = songDuration;
        this.artistName = artistName;
        this.genre = genre;
        this.songAlbumName = songAlbumName;
    }


    public int getSongID() {
        return songID;
    }

    public void setSongID(int songID) {
        this.songID = songID;
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public Time getSongDuration() {
        return songDuration;
    }

    public void setSongDuration(Time songDuration) {
        this.songDuration = songDuration;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getSongAlbumName() {
        return songAlbumName;
    }

    //constructor

    public void setSongAlbumName(String songAlbumName) {
        this.songAlbumName = songAlbumName;
    }

    //to string method

    @Override
    public String toString() {
        return "Operations.Song{" + "songID=" + songID + ", songName='" + songName + '\'' + ", songDuration=" + songDuration + ", artistName='" + artistName + '\'' + ", genre='" + genre + '\'' + ", songAlbumName='" + songAlbumName + '\'' + '}';
    }
}
