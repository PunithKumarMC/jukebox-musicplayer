package Operations;

import CustomExceptionhandling.UserException;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class SongOperation {
    Scanner sc = new Scanner(System.in);
    Connection con;
    List<Song> list;

    public void search(Connection con, List<Song> list) {
        try {
            System.out.println("1.Search Song by the Artist Name \n2.Search Song by Genre\n3.Search Song by Album Name \n4.Go Back to main menu");
            int response = sc.nextInt();
            switch (response) {
                case 1:
                    searchSongbyArtist(con, list);
                    break;
                case 2:
                    serchSongbyGenre(con, list);
                    break;
                case 3:
                    searchSongbyAlbumName(con, list);
                    break;
                case 4:
                    InnerHomePage in = new InnerHomePage();
                    System.out.println("Going back to main menu");
                    in.displaySongs(con);
                default:
                    System.out.println("Choose the correct option from the menu ");
                    search(con, list);
            }
        } catch (SQLException se) {
            se.printStackTrace();
        }

    }

    public void searchSongbyArtist(Connection con, List<Song> list) {

        InnerHomePage innerHomePage = new InnerHomePage();
        System.out.println("Enter the artist Name to search for Songs ");
        String artistName;
        //if (true)
        artistName = sc.nextLine();  //find artist then
        System.out.println("checking.......");
        System.out.format("%20s %20s %20s %20s", "SongName", "SongAlbumName", "Genre", "ArtistName\n");
        List<Song> artistlist = list.stream().filter((f) -> f.getArtistName().equalsIgnoreCase(artistName)).collect(Collectors.toList());
        if (artistlist.isEmpty()) {
            System.out.println("Artist Name searched is not present in our jukebox");
            System.out.println("Please search using correct details");
            searchSongbyArtist(con, list);
        } else {
            artistlist.stream().forEach((f) -> System.out.format("%20s %20s %20s %20s", "" + f.getSongName(), "" + f.getSongAlbumName(), "" + f.getGenre(), "" + f.getArtistName() + "\n"));
            System.out.println();  //differentiating the output from inner homepage
            System.out.println();
            innerHomePage.displaySongs(con);
        }


    }

    public void serchSongbyGenre(Connection con, List<Song> list) {

        InnerHomePage innerHomePage = new InnerHomePage();
        System.out.println("Enter the Genre to be searched");
        String genre = sc.next();
        System.out.println("checking.......");
        System.out.format("%20s %20s %20s %20s", "SongName", "SongAlbumName", "Genre", "ArtistName\n");
        List<Song> genreList = list.stream().filter((f) -> f.getGenre().equalsIgnoreCase(genre)).collect(Collectors.toList());
        if (genreList.isEmpty()) {
            System.out.println("Entered genre is not present in our Jukebox");
            System.out.println("Please search using correct details");
            serchSongbyGenre(con, list);
        } else {
            genreList.stream().forEach((l) -> System.out.format("%30s %20s %10s %20s", "" + l.getSongName(), "" + l.getSongAlbumName(), "" + l.getGenre(), "" + l.getArtistName() + "\n"));
            System.out.println();  //differentiating the output from inner homepage
            System.out.println();
            innerHomePage.displaySongs(con);
        }


    }

    public void searchSongbyAlbumName(Connection con, List<Song> list) throws SQLException {

        InnerHomePage innerHomePage = new InnerHomePage();
        System.out.println("Enter the Album name to be searched");
        String albumName = sc.nextLine();
        System.out.println("loading.......");
        System.out.format("%20s %20s %20s %20s", "SongName", "SongAlbumName", "Genre", "ArtistName\n");
        List<Song> albumList = list.stream().filter((f) -> f.getArtistName().equalsIgnoreCase(albumName)).collect(Collectors.toList());
        if (albumList.isEmpty()) {
            System.out.println("Entered album Name is not present in our Jukebox");
            System.out.println("Please search using correct details");
            searchSongbyAlbumName(con, list);
        } else {
            albumList.stream().forEach((l) -> System.out.format("%30s %20s %10s %20s", "" + l.getSongName(), "" + l.getSongAlbumName(), "" + l.getGenre(), "" + l.getArtistName() + "\n"));
            System.out.println();  //differentiating the output from inner homepage
            System.out.println();
            innerHomePage.displaySongs(con);
        }


    }

    public void sortSongListbyArtist(List<Song> list) {
        try {
            System.out.format("%30s %30s %10s %30s", "SongName", "SongAlbumName", "Genre", "ArtistName" + "\n");
            list.stream().sorted(Comparator.comparing((f) -> f.getArtistName())).forEach((f) -> System.out.format("%30s %30s %10s %30s", "" + f.getSongName(), "" + f.getSongAlbumName(), "" + f.getGenre(), "" + f.getArtistName() + "\n"));
            System.out.println();
            DefaultPage.homePage();
        } catch (SQLException | UserException se) {
            se.printStackTrace();
        } catch (UnsupportedAudioFileException | ClassNotFoundException | IOException | LineUnavailableException e) {
            throw new RuntimeException(e);
        }

    }
}
