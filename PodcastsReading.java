package Operations;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class PodcastsReading {
    public List<Podcasts> readPodcasts(Connection con) throws SQLException {
        List<Podcasts> listPodcasts = null;
        try {
            String query = "SELECT p.podcastsID,podcastsName,celebrityName,publishedDate,episodeNum FROM podcastsDetails p JOIN episodeDetails e ON p.podcastsID=e.podcastsID";
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            listPodcasts = new ArrayList<>();
            while (rs.next()) {
                listPodcasts.add(new Podcasts(rs.getInt("podcastsID"), rs.getString("podcastsName"), rs.getString("celebrityName"), rs.getDate("publishedDate"), rs.getInt("episodeNum")));
            }

        } catch (SQLException se) {
            se.printStackTrace();
        }
        return listPodcasts;
    }
}

