package controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLException;

public class Harvester {
    public String EpisodeData() throws IOException, SQLException {
        int x = 7993546;
        URL tvdb = new URL("https://api4.thetvdb.com/v4/episodes/" + x + "/extended");
        HttpURLConnection con = (HttpURLConnection) tvdb.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("accept", "application/json");
        int status = con.getResponseCode();
        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String InputLine;
        StringBuffer content = new StringBuffer();
        while ((InputLine = in.readLine()) != null) {
           content.append(InputLine);
        }
        in.close();
       //PreparedStatement ps = Main.db.prepareStatement("INSERT INTO Media ");
        System.out.println(InputLine);
        return ("complete");

    }
}
