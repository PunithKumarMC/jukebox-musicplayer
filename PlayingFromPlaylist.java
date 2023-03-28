package Operations;

import CustomExceptionhandling.NoSongPathException;
import CustomExceptionhandling.UserException;

import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PlayingFromPlaylist {
    static int r = 0, songID = 0;
    long pausetime;
    Clip clip;
    AudioInputStream audioInputStream;
    File file;
    Scanner sc = new Scanner(System.in);
    CreatingPlaylist creatingPlaylist;
    List<String> playlist;

    public void play(Connection con, int userid)  {
        try {
            System.out.println("1.Play from Songs Playlist \n2.Play from Podcasts Playlist \n3.View Songs Podcasts \n4.Go back");
            int c = sc.nextInt();
            switch (c) {
                case 1:
                    System.out.println("Playing from Song playlist");
                    playSong(con, userid);
                    break;
                case 2:
                    System.out.println("Playing from Podcasts playlist");
                    playpodcasts(con, userid);
                    break;
                case 3:
                    playSongsPodcasts(con);
                    break;
                case 4:
                    DefaultPage.homePage();
                    break;
                default:
                    System.out.println("invalid Choice");
                    play(con, userid);
            }
        }catch (SQLException | UserException | UnsupportedAudioFileException | LineUnavailableException | IOException |
                ClassNotFoundException se){
            se.printStackTrace();
        }

    }

    public void playSong(Connection con, int userid) {
        playlist = new ArrayList<String>();

        int userResponse1 = 0;
        try {
            String query = "SELECT playlistID,playlisttype,playlistName FROM playlistsdetails where userID=" + userid + "";
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            System.out.format("%20s %20s %20s", "playlistID", "playlistName", "playlisttype\n");
            while (rs.next()) {
                System.out.format("%20s %20s %20s ", "" + rs.getInt("playlistID"), "" + rs.getString("playlistName"), "" + rs.getString("playlisttype") + "\n");
            }
            if (r == 0) {
                System.out.println("Enter the playlist ID from which you want play the song");
                userResponse1 = sc.nextInt();
            }
            String query1 = "select sd.songpath FROM songdetails sd \n" + "where sd.songID in\n" + "(select sp.songID FROM songplaylistdetails sp\n" + "where sp.playlistID=?)";
            PreparedStatement pstmt = con.prepareStatement(query1);
            pstmt.setInt(1, userResponse1);
            ResultSet rs1 = pstmt.executeQuery();
            String temp = "";
            long pausetime = 0;
            while (rs1.next()) {
                temp = rs1.getString("songpath");
                playlist.add(temp);
                support(con, temp);
            }
        } catch (SQLException e) {
            e.getMessage();
        }

    }

    public void forwardingSong() {
        clip.setMicrosecondPosition(this.clip.getMicrosecondPosition() + 10000000);
        clip.start();
    }

    public void backwardingSong() {
        clip.setMicrosecondPosition(this.clip.getMicrosecondPosition() - 10000000);
        clip.start();
    }

    public void support(Connection con, String temp) {
        try {
            file = new File(temp);
            audioInputStream = AudioSystem.getAudioInputStream(file.getAbsoluteFile());
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();

            long duration = clip.getMicrosecondLength();
            duration = clip.getMicrosecondLength();
            System.out.println("Duration/length of the Playing Song is as shown below..");
            System.out.println("Duration = " + (duration / 1000000) + " sec");
            String userResponse = "";
            long finalDuration = (duration / 1000000);      //converting from microsecond
            final Timer t = new Timer(1000, new ActionListener() {   //block lambda  //milliseconds delay
                private long time = finalDuration; //10 seconds, for example

                public void actionPerformed(ActionEvent e) {          //action performed is a method to implement from class action listener
                    if (time >= 0) {
                        long s = ((time) % 60);   //converting into sec
                        long m = (((time) / 60) % 60); //converting into
                        long h = ((((time) / 60) / 60) % 60);   //converting into hour
                        System.out.println(h + " hours, " + m + " minutes " + s + " seconds");
                        time -= 1;
                    }
                }
            });
            t.start();
            while (!userResponse.equalsIgnoreCase("c")) {
                System.out.println("Please select the choice you want to do with song");
                System.out.println("'P' for Playing the song /Resuming the Song \n'S' for to Stop/Pause the playing song\n'R' is for restarting the song \n'G' for Fast Forwarding the Song \n'H' for Back Warding the Song \n'N' is for next song \n 'Z' is for previous Song \n'C' is for closing the playing Song");
                userResponse = sc.next();
                userResponse = userResponse.toUpperCase();
                switch (userResponse) {                     //while
                    case "P":
                        //status = "play";
                        clip.start();
                        System.out.println("playing the selected songID " + songID);
                        break;
                    case "S":
                        clip.stop();
                        System.out.println("Song has been stopped from playing " + songID);
                        t.stop();
                        break;
                    case "R":
                        t.stop();
                        clip.setMicrosecondPosition(0);
                        System.out.println("song is playing from the beginning");
                        break;
                    case "G":
                        System.out.println("Forwarding the Song by few seconds");
                        forwardingSong();
                        break;
                    case "H":
                        System.out.println("Back warding the Song by few seconds");
                        backwardingSong();
                        break;
                    case "N":  //doubt
                        t.stop();
                        r++;
                        songID++;
                        clip.stop();
                        System.out.println("Next Song is playing ");
                        return;
                    case "Z":
                        t.stop();
                        r++;
                        songID--;
                        clip.stop();
                        System.out.println("Previous Song is Playing");
                        return;
                    case "C":
                        t.stop();
                        clip.stop();
                        System.out.println("Thank you for playing the Songs,Closing the playlist");
                        DefaultPage.homePage();
                    default:
                        System.out.println("please choose the valid Option from the menu");
                        //play(con);
                }
            }
        } catch (UnsupportedAudioFileException | LineUnavailableException | IOException | UserException | SQLException |
                 ClassNotFoundException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void playpodcasts(Connection con, int userid) {
        int r = 0;
        sc = new Scanner(System.in);
        int userResponse1 = 0;
        try {
            String query = "SELECT playlistID,playlisttype,playlistName FROM playlistsdetails where userID=" + userid + "";
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            System.out.format("%20s %20s %20s", "playlistID", "playlistName", "playlisttype\n");
            while (rs.next()) {
                System.out.format("%20s %20s %20s ", "" + rs.getInt("playlistID"), "" + rs.getString("playlistName"), "" + rs.getString("playlisttype") + "\n");
            }
            if (r == 0) {
                System.out.println("Enter the playlist from which you want play the podcasts");
                userResponse1 = sc.nextInt();
            }
            else {
                System.out.println("No Podcasts in that playlist");
                play(con, userid);
            }
            String query1 = "select e.episodepath from episodedetails e where e.podcastsID in\n" + "(select p.podcastsID from podcastsdetails p where p.podcastsID in\n" + "(select pc.podcastsID from podcastslistdetails pc where pc.playlistID=?));";
            PreparedStatement pstmt = con.prepareStatement(query1);
            pstmt.setInt(1, userResponse1);
            ResultSet rs1 = pstmt.executeQuery();
            String temp = "";
            long pausetime = 0;
            while (rs1.next()) {
                temp = rs1.getString("e.episodepath");
                //playlist.add(temp);
                support(con, temp);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void playSongsPodcasts(Connection con) {
        try {
            System.out.println("Enter the playlistID to view the details");
            int id = sc.nextInt();
            String query = "SELECT s.songID,s.songName, e.episodeNum, p.podcastsName,e.celebrityName,s.songpath s,e.episodepath s  FROM songdetails s JOIN episodedetails e JOIN podcastsdetails p Join songplaylistdetails sp JOIN podcastslistdetails pc JOIN playlistsdetails pl\n" + "On pl.playlistID=sp.playlistID AND pl.playlistID=pc.playlistID AND s.songID=sp.songID AND p.podcastsID=e.podcastsID AND p.podcastsID=pc.podcastsID WHERE pl.playlistID=?";
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            System.out.format("%20s %20s %20s %20s %20s  ", "Song ID", "Song Name", "Episode Num", "Podcasts Name", "Celbrity Name\n");
            if (rs.next()) {
                System.out.format("%20s %20s %20s %20s %20s", "" + rs.getInt("s.songID"), "" + rs.getString("s.songName"), "" + rs.getInt("e.episodeNum"), "" + rs.getString("p.podcastsName"), "" + rs.getString("e.celebrityName") + "\n");
                while (rs.next()) {
                    System.out.format("%20s %20s %20s %20s %20s", "" + rs.getInt("s.songID"), "" + rs.getString("s.songName"), "" + rs.getInt("e.episodeNum"), "" + rs.getString("p.podcastsName"), "" + rs.getString("e.celebrityName") + "\n");
                }
            } else {
                throw new NoSongPathException("There are no Songs in with this playlistID");
            }
        } catch (SQLException se) {
            se.getMessage();
        } catch (NoSongPathException e) {
            System.out.println(e);
        }
    }

}
