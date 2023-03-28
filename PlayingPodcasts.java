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

public class PlayingPodcasts {
    static int r = 0, podcastsID = 0;
    Clip clip;
    AudioInputStream audioInputStream;
    File file;

    public void playingPodcasts(Connection con) {
        CreatingPlaylist creatingPlaylist = new CreatingPlaylist();
        SongsReading sr = new SongsReading();
        PodcastsReading pr = new PodcastsReading();
        Scanner sc = new Scanner(System.in);
        try {
            if (r == 0) {
                System.out.println("Enter the podcastsNum to play the podcasts");
                podcastsID = sc.nextInt();
            }
            String query = "SELECT e.episodepath FROM podcastsDetails p JOIN episodeDetails e ON p.podcastsID=e.podcastsID WHERE p.podcastsID=?";
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setInt(1, podcastsID);
            ResultSet rs = pstmt.executeQuery();
            String temp = "";
            while (rs.next()) {
                temp = rs.getString("e.episodepath");
            }
            file = new File(temp);
            audioInputStream = AudioSystem.getAudioInputStream(file.getAbsoluteFile());     //doubt exception c //use try catch
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
            long duration = clip.getMicrosecondLength();
            duration = clip.getMicrosecondLength();
            System.out.println("Duration/length of the Playing Song ");
            System.out.println("Duration = " + (duration / 1000000) + " sec");
            String userResponse = "";
            long finalDuration = (duration / 1000000);
            final Timer t = new Timer(1000, new ActionListener() {         //class that is used to run o code after a regular interval of time
                private long time = finalDuration; //10 seconds, for example      //actionlistner is interface when user clicks it automatically performs actions

                public void actionPerformed(ActionEvent e) {
                    if (time >= 0) {
                        long s = ((time) % 60);
                        long m = (((time) / 60) % 60);
                        long h = ((((time) / 60) / 60) % 60);
                        System.out.println(h + " hours, " + m + " minutes " + s + " seconds");
                        time -= 1;                                                          //decreasing the time by 1 sec
                    }
                }
            });
            t.start();                                                             //plays the podcasts if it is there in the database otherwise filenotfoundException is thrown
            while (!userResponse.equalsIgnoreCase("c")) {
                System.out.println("Please select the choice you want to do with podcasts");
                System.out.println("'P' for Playing the podcasts / Resuming the Podcasts \n'S' for to Stop/Pause the playing podcasts\n'R' is for restarting the podcasts \n'G' for Fast Forwarding the Podcasts \n'H' for Back Warding the Podcasts \n'N' is for next podcasts \n'Z' is for previous Podcasts  \n'C' is for closing the podcasts");
                userResponse = sc.next();
                userResponse = userResponse.toUpperCase();
                switch (userResponse) {                     //while
                    case "P":
                        clip.start();
                        t.start();
                        System.out.println("playing the selected podcastsID " + podcastsID);
                        break;
                    case "S":
                        t.stop();
                        clip.stop();
                        System.out.println("Podcasts has been stopped from playing " + podcastsID);
                        break;
                    case "R":
                        t.stop();
                        clip.setMicrosecondPosition(0);
                        System.out.println("Podcasts is playing from the beginning");
                        break;
                    case "G":
                        System.out.println("Forwarding the Podcasts by few seconds");
                        forwardingPodcasts();
                        break;
                    case "H":
                        System.out.println("Back warding the Podcasts by few seconds");
                        backwardingPodcasts();
                        break;
                    case "N":
                        r++;
                        podcastsID++;
                        clip.stop();
                        t.stop();
                        System.out.println("Next podcasts is playing ");        //doubt
                        playingPodcasts(con);
                        break;
                    case "Z":
                        r++;
                        podcastsID--;
                        clip.stop();
                        t.stop();
                        System.out.println("Previous podcasts is Playing");
                        playingPodcasts(con);
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
                        System.out.println("Closing the playlist");
                        DefaultPage.homePage();
                        break;
                    default:
                        System.out.println("please choose the correct option from the menu ");
                        playingPodcasts(con);
                }
            }
        } catch (SQLException | UnsupportedAudioFileException | IOException | LineUnavailableException | UserException |
                 ClassNotFoundException se) {
            se.printStackTrace();
        }
    }

    public void forwardingPodcasts() {
        clip.setMicrosecondPosition(this.clip.getMicrosecondPosition() + 10000000);
        clip.start();
    }

    public void backwardingPodcasts() {
        clip.setMicrosecondPosition(this.clip.getMicrosecondPosition() - 10000000);
        clip.start();
    }

    // test case
    public int podcastsPathChecking(String episodepath) {
        int temp = 0;
        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/wynkdb", "root", "Punith@9535");
            String query = "SELECT e.episodeNum FROM episodedetails e WHERE e.episodepath=?";
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setString(1, episodepath);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                temp = rs.getInt("e.episodeNum");
                System.out.println(temp);
            }
        } catch (SQLException se) {
            se.printStackTrace();
        }
        return temp;
    }
}
