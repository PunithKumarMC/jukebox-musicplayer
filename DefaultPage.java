package Operations;

import CustomExceptionhandling.UserException;
import Impl.MainImpl;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class DefaultPage {

    public static void homePage() throws SQLException, UserException, UnsupportedAudioFileException, LineUnavailableException, IOException, ClassNotFoundException {
        Connection con = null;
        try {
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/wynkdb", "root", "Punith@9535");
        } catch (SQLException se) {
            se.printStackTrace();
        }
        InnerHomePage sd = new InnerHomePage();
        SongsReading sr = new SongsReading();
        PodcastsReading pr = new PodcastsReading();
        CreatingPlaylist cp = new CreatingPlaylist();
        FindPLaylistID findPLaylistID = new FindPLaylistID();
        Scanner sc = new Scanner(System.in);
        System.out.println("Hi Music lover Welcome to the modern jukebox Stop-Listen-Go");
        System.out.println("please select the choice of services offered by the Jukebox");
        System.out.println("1.Display all the songs \n2.Display all podcasts \n3.User Playlists  \n4.Display all the Playlists \n5.Exit... Note that youn need to logIN again once you exit from the Juke box!!!");
        int choice = sc.nextInt();
        switch (choice) {
            case 1:
                sd.displaySongs(con);
                System.out.println();                                           //parent class
                break;
            case 2:
                sd.displayPodcasts(con);
                System.out.println();
                break;
            case 3:
                List<Song> list1 = sr.readSongs(con);
                List<Podcasts> list2 = pr.readPodcasts(con);
                cp.createplaylist(con, list1, list2);
                System.out.println();
                break;
            case 4:
                DisplayingPLaylist displayingPLaylist = new DisplayingPLaylist();
                //displayingPLaylist.displaysongplalists(con);
                displayingPLaylist.displayPlaylist(con);
                System.out.println();
                break;
            case 5:
                System.out.println("wait....taking you there");
                MainImpl.supportMain(con);
                break;
            default:
                System.out.println("Invalid choice, Please select the correct choice");
                homePage();
        }
    }
}
