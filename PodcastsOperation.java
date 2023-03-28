package Operations;

import CustomExceptionhandling.UserException;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class PodcastsOperation {
    Scanner sc = new Scanner(System.in);
    Connection con;

    public void searchPodcastsbyCelebrity(Connection con, List<Podcasts> list) {
        try {
            InnerHomePage innerHomePage = new InnerHomePage();
            System.out.println("Enter the celebrity Name to search for Songs ");
            String celebrityName = sc.nextLine();  //find celebrity then
            System.out.println("checking.......");
            System.out.format("%30s %20s %20s %20s", "podcastsName", "celebrityName", "publishedDate", "episodeNum" + "\n");
            List<Podcasts> celebritylist = list.stream().filter((l) -> l.getCelebrityName().equalsIgnoreCase(celebrityName)).collect(Collectors.toList());
            if (celebritylist.isEmpty()) {
                System.out.println("Celebrity Name searched is not present in our jukebox");
                System.out.println("Please search using correct details");
                searchPodcastsbyCelebrity(con, list);
            } else {
                celebritylist.stream().forEach((f) -> System.out.format("%20s %20s %20s %20s", "" + f.getPodcastsName(), "" + f.getCelebrityName(), "" + f.getPublishedDate(), "" + f.getEpisodeNum() + "\n"));
                innerHomePage.displayPodcasts(con);
            }
        } catch (SQLException | UserException se) {
            se.printStackTrace();
        } catch (UnsupportedAudioFileException | LineUnavailableException | IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

    public void searchPodcastsbyDate(Connection con, List<Podcasts> list) {
        try {
            InnerHomePage innerHomePage = new InnerHomePage();
            System.out.println("Enter the Date of Podcasts to be searched");
            String date = sc.next();
            Date cordate = new Date(date);
            System.out.println("Checking........");           //mm/dd/yyyy while entering
            System.out.format("%30s %20s %10s %20s", "podcastsName", "celebrityName", "publishedDate", "episodeNum" + "\n");
            List<Podcasts> datelist = list.stream().filter((f) -> f.getPublishedDate().equals(cordate)).collect(Collectors.toList());
            if (datelist.isEmpty()) {
                System.out.println("Date searched is not present in our jukebox");
                System.out.println("Please search using correct episode date details");
                searchPodcastsbyDate(con, list);
            } else {
                datelist.stream().forEach((f) -> System.out.format("%30s %20s %10s %20s", "" + f.getPodcastsName(), "" + f.getCelebrityName(), "" + f.getPublishedDate(), "" + f.getEpisodeNum() + "\n"));
                innerHomePage.displayPodcasts(con);
            }
        } catch (SQLException | UserException | UnsupportedAudioFileException | LineUnavailableException | IOException |
                 ClassNotFoundException se) {
            se.printStackTrace();
        }


    }

    public void sortPodcastsbyCelebrity(List<Podcasts> list) {
        try {
            System.out.format("%30s %20s %10s %20s", "podcastsName", "celebrityName", "publishedDate", "episodeNum" + "\n");
            list.stream().sorted(Comparator.comparing(f -> f.getCelebrityName())).forEach((f) -> System.out.format("%30s %20s %10s %20s", "" + f.getPodcastsName(), "" + f.getCelebrityName(), "" + f.getPublishedDate(), "" + f.getEpisodeNum() + "\n"));
            System.out.println();
            DefaultPage.homePage();
        } catch (SQLException | UserException | UnsupportedAudioFileException | LineUnavailableException | IOException |
                 ClassNotFoundException se) {
            se.printStackTrace();
        }

    }
}
