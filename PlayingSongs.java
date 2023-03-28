package Operations;

import CustomExceptionhandling.UserException;

import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.Scanner;

public class PlayingSongs {
    static int r = 0, songID = 0;
    String status;
    long pausetime;
    Clip clip;
    AudioInputStream audioInputStream;
    File file;

    public void playSongs(Connection con) throws UserException {
        CreatingPlaylist creatingPlaylist = new CreatingPlaylist();
        SongsReading sr = new SongsReading();
        PodcastsReading pr = new PodcastsReading();
        Scanner sc = new Scanner(System.in);
        try {
            if (r == 0) {
                System.out.println("Enter the songID to play the song");
                songID = sc.nextInt();
            }
            String query = "SELECT songPath FROM songDetails WHERE songID=?";
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setInt(1, songID);
            ResultSet rs = pstmt.executeQuery();
            String temp = "";
            long pausetime = 0;
            while (rs.next()) {
                temp = rs.getString("songpath");
                System.out.println(temp);
                throw new UserException("file not found");
            }
            file = new File(temp);
            audioInputStream = AudioSystem.getAudioInputStream(file.getAbsoluteFile());     //doubt exception c //use try catch
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();

            long duration = clip.getMicrosecondLength();
            duration = clip.getMicrosecondLength();
            System.out.println("Duration/length of the Playing Operations.Song is as shown below..");
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
                System.out.println("'P' for Playing the song /Resuming the Operations.Song \n'S' for to Stop/Pause the playing song\n'R' is for restarting the song \n'G' for Fast Forwarding the Operations.Song \n'H' for Back Warding the Operations.Song \n'N' is for next song \n 'Z' is for previous Operations.Song \n'C' is for closing the playing Operations.Song");
                userResponse = sc.next();
                userResponse = userResponse.toUpperCase();
                switch (userResponse) {                     //while
                    case "P":
                        status = "play";
                        clip.start();
                        System.out.println("playing the selected songID " + songID);
                        break;
                    case "S":
                        clip.stop();
                        System.out.println("Operations.Song has been stopped from playing " + songID);
                        t.stop();
                        break;
                    case "R":
                        t.stop();
                        clip.setMicrosecondPosition(0);
                        System.out.println("song is playing from the beginning");
                        break;
                    case "G":
                        System.out.println("Forwarding the Operations.Song by few seconds");
                        forwardingSong();
                        break;
                    case "H":
                        System.out.println("Back warding the Operations.Song by few seconds");
                        backwardingSong();
                        break;
                    case "N":  //doubt
                        t.stop();
                        r++;
                        songID++;
                        clip.stop();
                        System.out.println("Next Operations.Song is playing ");        //doubt
                        playSongs(con);
                        break;
                    case "Z":
                        t.stop();
                        r++;
                        songID--;
                        clip.stop();
                        System.out.println("Previous Operations.Song is Playing");
                        playSongs(con);
                        break;
                    /*case "A":
                        t.stop();
                        clip.stop();
                        System.out.println("Loading......");
                        List<Operations.Song> list1 = sr.readSongs(con);
                        List<Operations.Podcasts> list2 = pr.readPodcasts(con);
                        creatingPlaylist.createplaylist(con,list1,list2);
                        break;*/
                    case "C":
                        t.stop();
                        clip.stop();
                        System.out.println("Thank you for playing the Songs,Closing the playlist");
                        DefaultPage.homePage();
                    default:
                        System.out.println("please choose the valid Option from the menu");
                        playSongs(con);
                }
            }
        } catch (SQLException | UnsupportedAudioFileException | IOException | LineUnavailableException |
                 ClassNotFoundException se) {
            se.printStackTrace();
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

    //test case
    public int songPathChecking(String songpath) {
        int temp = 0;
        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/wynkdb", "root", "Punith@9535");
            String query = "SELECT songID FROM songDetails WHERE songpath=?";
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setString(1, songpath);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                temp = rs.getInt("songID");
                System.out.println(temp);
            }
        } catch (SQLException se) {
            se.printStackTrace();
        }
        return temp;
    }
}
