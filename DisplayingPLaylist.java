package Operations;

import CustomExceptionhandling.UserException;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class DisplayingPLaylist {
    Scanner sc = new Scanner(System.in);

    public void displayPlaylist(Connection con) {
        try {
            System.out.println("Enter the user ID to display playlist");
            int userID = sc.nextInt();
            String query = "select p.playlistID,p.playlisttype,p.userID,sp.songID,pp.podcastsID,p.playlistName from playlistsdetails p\n" + "left outer join songplaylistdetails sp\n" + "on p.playlistID=sp.playlistID\n" + "left outer join podcastslistdetails pp\n" + "on p.playlistID=pp.playlistID\n" + "where p.userID=?";
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setInt(1, userID);
            ResultSet rs = pstmt.executeQuery();
            System.out.format("%20s %20s %20s %20s %20s %20s", "playlistID", "playlisttype", "userID", "songID", "podcastsID", "playlistName\n");
            if (rs.next()) {
                while (rs.next()) {
                    System.out.format("%20s %20s %20s %20s %20s %20s", "" + rs.getInt("p.playlistID"), "" + rs.getString("p.playlisttype"), "" + rs.getInt("p.userID"), "" + rs.getInt("sp.songID"), "" + rs.getString("pp.podcastsID"), "" + rs.getString("p.playlistName") + "\n");
                }
                //Operations.DefaultPage.homePage();
                for (; ; ) {
                    System.out.println("Enter the playlistID to view Songs");
                    System.out.println("1.Song playlist \n 2.Podcasts Playlist \n3.Songs and podcasts Playlist \n4.Go back to menu");
                    int userResponse = sc.nextInt();
                    switch (userResponse) {
                        case 1:
                            displaySongsPlaylist(con);
                            break;
                        case 2:
                            displayPodcastsPlalist(con);
                            break;
                        case 3:
                            displaySongsPodcastsPlaylist(con);
                            break;
                        case 4:
                            DefaultPage.homePage();
                        default:
                            System.out.println("Invalid Choice");
                            displayPlaylist(con);
                    }
                }
            } else {
                System.out.println("Please enter the correct userID to display the playlists");
                displayPlaylist(con);
            }
        } catch (UnsupportedAudioFileException | ClassNotFoundException | LineUnavailableException | IOException |
                 UserException | SQLException e) {
            e.printStackTrace();
        }
    }


    public void displaySongsPlaylist(Connection con) {
        try {
            //displayPlaylist(con);
            System.out.println("Enter the playlistID to view Songs");
            int pId = sc.nextInt();
            String query = "SELECT s.songName,s.songDuration,s.genre FROM songdetails s WHERE s.songID IN (SELECT sp.songID FROM songplaylistdetails sp WHERE sp.playlistId=?)";
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setInt(1, pId);
            ResultSet rs = pstmt.executeQuery();
            System.out.format("%20s %20s %20s ", "Song Name", "Song Duration", "Genre\n");
            while (rs.next()) {
                System.out.format("%20s %20s %20s", "" + rs.getString("s.songName"), "" + rs.getString("s.songDuration"), "" + rs.getString("s.genre") + "\n");
            }
        } catch (SQLException se) {
            se.getMessage();
        }
    }

    public void displayPodcastsPlalist(Connection con) {
        try {
            System.out.println("Enter the PlaylistID to view Operations.Podcasts");
            int id = sc.nextInt();
            String query = "SELECT e.episodeNum,e.celebrityName FROM episodedetails e WHERE e.podcastsID IN (SELECT p.podcastsID FROM podcastsdetails p WHERE p.podcastsID IN (SELECT pl.podcastsID FROM podcastslistdetails pl WHERE pl.playlistID=?))";
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            System.out.format("%20s %20s ", "Episode Number", "Celbrity Name\n");
            while (rs.next()) {
                System.out.format("%20s %20s", "" + rs.getInt("e.episodeNum"), "" + rs.getString("e.celebrityName") + "\n");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void displaySongsPodcastsPlaylist(Connection con) {
        try {
            System.out.println("Enter the playlistID to view the details");
            int id = sc.nextInt();
            String query = "SELECT s.songID,s.songName, e.episodeNum, p.podcastsName,e.celebrityName FROM songdetails s JOIN episodedetails e JOIN podcastsdetails p Join songplaylistdetails sp JOIN podcastslistdetails pc JOIN playlistsdetails pl\n" + "On pl.playlistID=sp.playlistID AND pl.playlistID=pc.playlistID AND s.songID=sp.songID AND p.podcastsID=e.podcastsID AND p.podcastsID=pc.podcastsID WHERE pl.playlistID=?";
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            System.out.format("%20s %20s %20s %20s %20s  ", "Song ID", "Song Name", "Episode Num", "Podcasts Name", "Celbrity Name\n");
            while (rs.next()) {
                System.out.format("%20s %20s %20s %20s %20s", "" + rs.getInt("s.songID"), "" + rs.getString("s.songName"), "" + rs.getInt("e.episodeNum"), "" + rs.getString("p.podcastsName"), "" + rs.getString("e.celebrityName") + "\n");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
