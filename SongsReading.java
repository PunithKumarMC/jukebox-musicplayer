package Operations;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class SongsReading {
    public List<Song> readSongs(Connection con) throws SQLException {
        List<Song> listSong = null;
        try {
            String query = "SELECT songID,songName,songDuration,artistName,genre,songAlbumName FROM songDetails s JOIN albumDetails a ON s.songAlbumID=a.songAlbumID ";
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            listSong = new ArrayList<>();
            while (rs.next()) {
                listSong.add(new Song(rs.getInt("songID"), rs.getString("songName"), rs.getTime("songDuration"), rs.getString("artistName"), rs.getString("genre"), rs.getString("songAlbumName")));
            }
        } catch (SQLException se) {
            se.printStackTrace();
        }
        return listSong;

    }
}

