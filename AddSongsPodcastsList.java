package Operations;

import CustomExceptionhandling.UserException;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class AddSongsPodcastsList {
    Scanner sc = new Scanner(System.in);
    FindPLaylistID findPLaylistID = new FindPLaylistID();

    public void songplaylist(Connection con, List<Song> list, int userid) throws UserException, SQLException, UnsupportedAudioFileException, LineUnavailableException, IOException, ClassNotFoundException {
        String userresponse = "";
        String playListName = "";
        while (!userresponse.equalsIgnoreCase("N")) {
            System.out.println("Whether you want add the Songs Yes 'Y' OR NO 'N'");
            userresponse = sc.next();
            switch (userresponse) {
                case "Y":
                    findPLaylistID.findPlaylist(con, userid);
                    System.out.println("Enter the Playlist Name  in which you want add the Song");
                    playListName = sc.next();
                    addingSong(con, list, userid, playListName);
                case "N":
                    System.out.println("Exiting from adding Song");
                    DefaultPage.homePage();
                default:
                    System.out.println("Invalid choice");
                    songplaylist(con, list, userid);
            }
        }
    }

    public void addingSong(Connection con, List<Song> list, int userid, String playlistName) {
        try {
            System.out.println("Enter the SongID to add to your Song playlist");
            int id = sc.nextInt();
            Optional<Song> sList = list.stream().filter((f) -> f.getSongID() == id).findAny();
            if (sList.isPresent()) {
                int songid = sList.get().getSongID();
                //autoInsertDetails(con, "Songs", playlistName, userid,list);
                //findPLaylistID.findPlaylist(con, userid);
                String query1 = "SELECT playlistID,playlistName,playlisttype,userID FROM playlistsDetails WHERE playlistName=?"; //all will get
                PreparedStatement pstmt = con.prepareStatement(query1);
                pstmt.setString(1, playlistName);
                ResultSet rs = pstmt.executeQuery();
                int playlistID = 0;
                String playlisttype = "";
                int userID = 0;
                String pName = "";
                System.out.format("%20s %20s %20s %20s", "playlistID", "PlayList Name", "playlist Type", "userID\n");
                while (rs.next()) {
                    playlistID = rs.getInt("playlistID");
                    pName = rs.getString("playlistName");
                    playlisttype = rs.getString("playlisttype");
                    userID = rs.getInt("userID");
                    System.out.format("%20s %20s %20s %20s", "" + playlistID, "" + pName, "" + playlisttype, "" + userID + "\n");
                }
                System.out.println("Enter the Playlist ID you want add the song");
                int userplaylistID = sc.nextInt();
                songInsert(con, id, userplaylistID);
                System.out.println("Song added to playlistID " + userplaylistID + " to user " + userid);
                songplaylist(con, list, userid);
            } else {
                System.out.println("Entered Song ID is not present in our Jukebox please check the Song ID from Display Song option");
                DefaultPage.homePage();
            }
        } catch (SQLException | UserException | UnsupportedAudioFileException | LineUnavailableException | IOException |
                 ClassNotFoundException se) {
            se.printStackTrace();
        }
    }

    public void songInsert(Connection con, int id, int playlistID) {
        try {
            String query = "INSERT INTO songplaylistDetails (songID,playlistID) VALUES (?,?)";
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setInt(1, id);
            pstmt.setInt(2, playlistID);
            int count = pstmt.executeUpdate();
            if (count == 0) {
                System.out.println("song is not added to the Song playlist");
            } else {
                System.out.println("Song " + id + " is added to the Song playlist " + playlistID);
            }
        } catch (SQLException se) {
            se.printStackTrace();
        }
    }

    public void addingPodcasts(Connection con, List<Podcasts> list, int userid, String playlistName) {
        try {
            System.out.println("Enter the PodcastsID to add to your Operations.Song playlist");
            int id = sc.nextInt();
            Optional<Podcasts> sList = list.stream().filter((f) -> f.getPodcastsID() == id).findAny();
            if (sList.isPresent()) {
                int songid = sList.get().getPodcastsID();
                //autoInsertDetails(con, "Songs", playlistName, userid,list);
                //findPLaylistID.findPlaylist(con, userid);
                String query1 = "SELECT playlistID,playlistName,playlisttype,userID FROM playlistsDetails WHERE playlistName=?"; //all will get
                PreparedStatement pstmt = con.prepareStatement(query1);
                pstmt.setString(1, playlistName);
                ResultSet rs = pstmt.executeQuery();
                int playlistID = 0;
                String playlisttype = "";
                int userID = 0;
                String pName = "";
                System.out.format("%20s %20s %20s %20s", "playlistID", "PlayList Name", "playlist Type", "userID\n");
                while (rs.next()) {
                    playlistID = rs.getInt("playlistID");
                    pName = rs.getString("playlistName");
                    playlisttype = rs.getString("playlisttype");
                    userID = rs.getInt("userID");
                    System.out.format("%20s %20s %20s %20s", "" + playlistID, "" + pName, "" + playlisttype, "" + userID + "\n");
                }
                System.out.println("Enter the Playlist ID you want add the Podcasts");
                int userplaylistID = sc.nextInt();
                podcastsInsert(con, id, userplaylistID);
                System.out.println("Podcasts added to playlistID " + userplaylistID + " to user " + userid);
                podcastsplaylist(con, list, userid);
            } else {
                System.out.println("Entered Podcasts ID is not present in our Jukebox please check the PodcastsID from Display Podcasts option");
                DefaultPage.homePage();
            }
        } catch (SQLException | UserException | UnsupportedAudioFileException | LineUnavailableException | IOException |
                 ClassNotFoundException se) {
            se.printStackTrace();
        }
    }

    public void podcastsInsert(Connection con, int id, int playlistID) {
        try {
            String query = "INSERT INTO podcastslistdetails (podcastsID,playlistID) VALUES (?,?)";
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setInt(1, id);
            pstmt.setInt(2, playlistID);
            int count = pstmt.executeUpdate();
            if (count == 0) {
                System.out.println("podcasts is not added to the Podcasts playlist");
            } else {
                System.out.println("Podcasts " + id + " is added to the Podcasts playlist " + playlistID);
            }
        } catch (SQLException se) {
            se.printStackTrace();
        }
    }

    public void podcastsplaylist(Connection con, List<Podcasts> list, int userid) throws UserException, SQLException, UnsupportedAudioFileException, LineUnavailableException, IOException, ClassNotFoundException {
        String userresponse = "";
        String playListName = "";
        while (!userresponse.equalsIgnoreCase("N")) {
            System.out.println("Whether you want add the Podcasts Yes 'Y' OR NO 'N'");
            userresponse = sc.next();
            switch (userresponse) {
                case "Y":
                    findPLaylistID.findPlaylist(con, userid);
                    System.out.println("Enter the Playlist Name in which you want add the Podcasts");
                    playListName = sc.next();
                    addingPodcasts(con, list, userid, playListName);
                case "N":
                    System.out.println("Exiting from adding Podcasts");
                    DefaultPage.homePage();
                default:
                    System.out.println("Invalid choice");
                    podcastsplaylist(con, list, userid);
            }
        }
    }


    public void songpodcastsplaylist(Connection con, List<Song> list1, List<Podcasts> list2, int userid) throws UnsupportedAudioFileException, UserException, SQLException, LineUnavailableException, IOException, ClassNotFoundException {
        String userresponse = "";
        String playListName = "";
        while (!userresponse.equalsIgnoreCase("N")) {
            System.out.println("Whether you want add the Songs/Podcasts Yes 'Y' OR NO 'N'");
            userresponse = sc.next();
            switch (userresponse) {
                case "Y":
                    findPLaylistID.findPlaylist(con, userid);
                    System.out.println("Enter the Playlist Name  in which you want add the Podcasts");
                    playListName = sc.next();
                    try {
                        System.out.println("Enter the PodcastsID to add to your Song playlist");
                        int id = sc.nextInt();
                        Optional<Podcasts> sList = list2.stream().filter((f) -> f.getPodcastsID() == id).findAny();
                        if (sList.isPresent()) {
                            int songid = sList.get().getPodcastsID();
                            //autoInsertDetails(con, "Songs", playlistName, userid,list);
                            //findPLaylistID.findPlaylist(con, userid);
                            String query1 = "SELECT playlistID,playlistName,playlisttype,userID FROM playlistsDetails WHERE playlistName=?"; //all will get
                            PreparedStatement pstmt = con.prepareStatement(query1);
                            pstmt.setString(1, playListName);
                            ResultSet rs = pstmt.executeQuery();
                            int playlistID = 0;
                            String playlisttype = "";
                            int userID = 0;
                            String pName = "";
                            System.out.format("%20s %20s %20s %20s", "playlistID", "PlayList Name", "playlist Type", "userID\n");
                            while (rs.next()) {
                                playlistID = rs.getInt("playlistID");
                                pName = rs.getString("playlistName");
                                playlisttype = rs.getString("playlisttype");
                                userID = rs.getInt("userID");
                                System.out.format("%20s %20s %20s %20s", "" + playlistID, "" + pName, "" + playlisttype, "" + userID + "\n");
                            }
                            System.out.println("Enter the Playlist ID you want add the Podcasts");
                            int userplaylistID = sc.nextInt();
                            podcastsInsert(con, id, userplaylistID);
                            System.out.println("Podcasts added to playlistID " + userplaylistID + " to user " + userid);
                            //podcastsplaylist(con, list2, userid);
                        } else {
                            System.out.println("Entered Podcasts ID is not present in our Jukebox please check the PodcastsID from Display Podcasts option");
                            DefaultPage.homePage();
                        }
                    } catch (SQLException | UserException | UnsupportedAudioFileException | LineUnavailableException |
                             IOException | ClassNotFoundException se) {
                        se.printStackTrace();
                    }

                    try {
                        System.out.println("Enter the SongID to add to your Song playlist");
                        int id = sc.nextInt();
                        Optional<Song> sList = list1.stream().filter((f) -> f.getSongID() == id).findAny();
                        if (sList.isPresent()) {
                            int songid = sList.get().getSongID();
                            //autoInsertDetails(con, "Songs", playlistName, userid,list);
                            //findPLaylistID.findPlaylist(con, userid);
                            String query1 = "SELECT playlistID,playlistName,playlisttype,userID FROM playlistsDetails WHERE playlistName=?"; //all will get
                            PreparedStatement pstmt = con.prepareStatement(query1);
                            pstmt.setString(1, playListName);
                            ResultSet rs = pstmt.executeQuery();
                            int playlistID = 0;
                            String playlisttype = "";
                            int userID = 0;
                            String pName = "";
                            System.out.format("%20s %20s %20s %20s", "playlistID", "PlayList Name", "playlist Type", "userID\n");
                            while (rs.next()) {
                                playlistID = rs.getInt("playlistID");
                                pName = rs.getString("playlistName");
                                playlisttype = rs.getString("playlisttype");
                                userID = rs.getInt("userID");
                                System.out.format("%20s %20s %20s %20s", "" + playlistID, "" + pName, "" + playlisttype, "" + userID + "\n");
                            }
                            System.out.println("Enter the Playlist ID you want add the song");
                            int userplaylistID = sc.nextInt();
                            songInsert(con, id, userplaylistID);
                            System.out.println("Song added to playlistID " + userplaylistID + " to user " + userid);
                            //songplaylist(con, list1, userid);
                            podcastsplaylist(con, list2, userid);
                        } else {
                            System.out.println("Entered Song ID is not present in our Jukebox please check the Song ID from Display Song option");
                            DefaultPage.homePage();
                        }
                    } catch (SQLException | UserException | UnsupportedAudioFileException | LineUnavailableException |
                             IOException | ClassNotFoundException se) {
                        se.printStackTrace();
                    }
                case "N":
                    System.out.println("Exiting from adding Song/Podcasts");
                    DefaultPage.homePage();
                default:
                    System.out.println("Invalid choice");
                    songpodcastsplaylist(con, list1, list2, userid);
            }
        }
    }


    //common method for all cases
    public void createUserPlaylist(Connection con, String playlistType, int userID) {
        try {
            System.out.println("Enter the playlist name you want create");
            String pName = sc.next();
            String query = "INSERT INTO playlistsDetails (playlistName,playlisttype,userID) VALUES(?,?,?)";
            PreparedStatement pstmt1 = con.prepareStatement(query);
            pstmt1.setString(1, pName);
            pstmt1.setString(2, playlistType);
            pstmt1.setInt(3, userID);
            int count = pstmt1.executeUpdate();
            if (count == 0) {
                System.out.println("Playlist is not created");

            } else {
                System.out.println("Playlist of type " + playlistType + " is created for user of " + userID);
                DefaultPage.homePage();
            }

        } catch (SQLException | UnsupportedAudioFileException | LineUnavailableException | UserException | IOException |
                 ClassNotFoundException se) {
            se.printStackTrace();
        }
    }

}
