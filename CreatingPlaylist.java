package Operations;

import CustomExceptionhandling.UserException;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CreatingPlaylist {
    Scanner sc = new Scanner(System.in);
    AddSongsPodcastsList addSongsPodcastsList = new AddSongsPodcastsList();
    List<Song> listSong = new ArrayList<>();
    SongsReading songsReading = new SongsReading();
    List<Podcasts> listPodcasts = new ArrayList<>();
    PodcastsReading podcastsReading = new PodcastsReading();
    PlayingFromPlaylist playingFromPlaylist = new PlayingFromPlaylist();


    public void createplaylist(Connection con, List<Song> songList, List<Podcasts> podcastsList) {
        try {
            System.out.println("Enter the userID to add Songs /podcasts to your account ");
            int userID = sc.nextInt();
            String query = "SELECT userID FROM userDetails";
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            int id = 0;
            boolean check = false;
            while (rs.next()) {
                id = rs.getInt("userID");
                if (id == userID) {
                    check = true;
                    break;
                }
            }
            if (!check) {
                System.out.println("You have no account with the given userID ");
                System.out.println("Please enter the correct userID to add Songs");
                createplaylist(con, songList, podcastsList);
            } else {  //how to add to the existing playlist
                System.out.println("Please select what you want to do...");
                System.out.println("1.Add Songs to the existing Playlist \n2.Podcasts to the Playlist \n3.Song/podcasts to the Playlist \n4.Play from playlist \n5.Create playlist \n6.Go back to main menu");
                int choice = sc.nextInt();
                switch (choice) {
                    case 1:
                        addSongsPodcastsList.songplaylist(con, songList, userID);
                        //createplaylist(con, songList, podcastsList);
                        break;
                    case 2:
                        addSongsPodcastsList.podcastsplaylist(con, podcastsList, userID);
                        //createplaylist(con, songList, podcastsList);
                        break;
                    case 3:
                        addSongsPodcastsList.songpodcastsplaylist(con, songList, podcastsList, userID);
                        //createplaylist(con, songList, podcastsList);
                        break;
                    case 4:
                        System.out.println("wait..........");
                        playingFromPlaylist.play(con, userID);
                        break;
                    case 5:
                        System.out.println("Enter the type of playlist (SONGS/PODCASTS/Combination of SONGS and PODCASTS)");
                        String type = sc.next();
                        addSongsPodcastsList.createUserPlaylist(con, type, userID);
                        break;
                    case 6:
                        System.out.println("Going back main menu, please wait....");
                        DefaultPage.homePage();
                }
            }
        } catch (SQLException | UserException | UnsupportedAudioFileException | LineUnavailableException | IOException |
                 ClassNotFoundException se) {
            se.printStackTrace();
        }

    }
}
