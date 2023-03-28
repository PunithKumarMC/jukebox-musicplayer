package Operations;

import CustomExceptionhandling.UserException;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class InnerHomePage {
    List<Song> listSong;
    Scanner sc;
    SongOperation songOperation;
    SongsReading songsReading;
    PodcastsReading podcastsReading;
    List<Podcasts> listPodcasts;
    PodcastsOperation podcastsOperation;
    PlayingSongs playingSongs;
    PlayingPodcasts playingPodcasts;

    public InnerHomePage() {
        listSong = new ArrayList<>();
        sc = new Scanner(System.in);
        songOperation = new SongOperation();
        songsReading = new SongsReading();
        podcastsReading = new PodcastsReading();
        listPodcasts = new ArrayList<>();
        podcastsOperation = new PodcastsOperation();
        playingSongs = new PlayingSongs();
        playingPodcasts = new PlayingPodcasts();
    }

    public void displaySongs(Connection con) {
        try {
            System.out.println("Displaying all the Songs");
            listSong = songsReading.readSongs(con);
            System.out.format("%8s %30s %10s %25s %20s %10s", "SongID", "songName", "songDuration", "artistName", "Operations.Song Album", "genre\n");
            listSong.stream().forEach((f) -> System.out.format("%8s %30s %10s %25s %20s %10s", "" + f.getSongID(), "" + f.getSongName(), "" + f.getSongDuration(), "" + f.getArtistName(), "" + f.getSongAlbumName(), "" + f.getGenre() + "\n"));

            System.out.println();
            System.out.println("what you want do with the Songs ");
            System.out.println("1.Play the Songs \n2.Search Songs by category \n3.Sort the Songs in Alphabetical Order of their Artist Name \n4.Go back to Main menu");
            int choice = sc.nextInt();
            switch (choice) {
                case 1:
                    playingSongs.playSongs(con);
                    System.out.println();
                    break;
                case 2:
                    songOperation.search(con, listSong);
                    System.out.println();
                    break;
                case 3:
                    System.out.println("Below table shows the artist name in sorted order");
                    songOperation.sortSongListbyArtist(listSong);
                    System.out.println();
                    break;
                case 4:
                    DefaultPage.homePage(); //calling the homepage to start again
                    System.out.println();  //defaultpage
                default:
                    System.out.println("Please select the valid choice of Songs to continue enjoying the Jukebox");
                    displaySongs(con);
            }
        } catch (SQLException | UserException | UnsupportedAudioFileException | LineUnavailableException | IOException |
                 ClassNotFoundException se) {
            se.printStackTrace();
        }
    }

    public void displayPodcasts(Connection con) throws SQLException, UserException, UnsupportedAudioFileException, LineUnavailableException, IOException, ClassNotFoundException {
        System.out.println("Displaying all the podcasts");
        listPodcasts = podcastsReading.readPodcasts(con);
        System.out.format("%5s %25s %25s %20s %5s", "podcastsID", "podcastsName", "celebrityName", "publishedDate", "episodeNum\n");
        listPodcasts.stream().forEach(f -> System.out.format("%5s %25s %25s %20s %5s", "" + f.getPodcastsID(), "" + f.getPodcastsName(), "" + f.getCelebrityName(), "" + f.getPublishedDate(), "" + f.getEpisodeNum() + "\n"));

        System.out.println();
        System.out.println("what you want do with the podcasts");
        System.out.println("1.Play the podcasts \n2.Search podcasts by using celebrity Name \n3.Search podcasts by released Date \n4.Sort podcasts by using the celebrity Name \n5. Go back to Main menu");
        int choice = sc.nextInt();
        switch (choice) {
            case 1:
                playingPodcasts.playingPodcasts(con);
                System.out.println();
                break;
            case 2:
                podcastsOperation.searchPodcastsbyCelebrity(con, listPodcasts);
                System.out.println();
                break;
            case 3:
                podcastsOperation.searchPodcastsbyDate(con, listPodcasts);
                System.out.println();
                break;
            case 4:
                System.out.println("Below table shows the celebrity name in sorted order");
                podcastsOperation.sortPodcastsbyCelebrity(listPodcasts);
                System.out.println();
                break;
            case 5:
                System.out.println("wait......");
                DefaultPage.homePage();
                //defaultpage
            default:
                System.out.println("Please select the valid choice of podcasts to continue enjoying Jukebox");
                displayPodcasts(con);
        }
    }
}
