package Operations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FindPLaylistID {
    public void findPlaylist(Connection con, int userID) {
        try {
            System.out.println("Playlist that belongs to the user of " + userID);
            String query = "SELECT playlistID,playlistName,playlisttype,userID FROM playlistsDetails WHERE userID=? ";
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setInt(1, userID);
            ResultSet rs = pstmt.executeQuery();
            System.out.format("%20s %20s %20s %20s", "playlistID", "PlayList name", "playlisttype", "userID\n");
            while (rs.next()) {
                System.out.format("%20s %20s %20s  %20s", "" + rs.getInt("playlistID"), "" + rs.getString("playlistName"), "" + rs.getString("playlisttype"), "" + rs.getInt("userID") + "\n");
            }
        } catch (SQLException se) {
            se.printStackTrace();
        }
    }
}
