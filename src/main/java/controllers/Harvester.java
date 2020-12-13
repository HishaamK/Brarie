package controllers;

import com.uwetrottmann.thetvdb.TheTvdb;
import com.uwetrottmann.thetvdb.entities.Episode;
import com.uwetrottmann.thetvdb.entities.EpisodeResponse;
import com.uwetrottmann.thetvdb.entities.EpisodesResponse;
import com.uwetrottmann.thetvdb.entities.SeriesResponse;
import retrofit2.*;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import server.Main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Harvester {
    public void EpisodeData() throws IOException, SQLException {
      /*  int x = 7993546;
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

       */
        TheTvdb TVDB = new TheTvdb("0783d5e9-1707-4a53-842e-f16671eb6568");


            int x = 5;
            Response<EpisodeResponse> response = TVDB.episodes()
                    .get(x, "en")
                    .execute();
            if (response.isSuccessful()) {
                Episode episode = response.body().data;
                String Title = episode.episodeName;
                int Number = episode.airedEpisodeNumber;
                String ReleaseDate = episode.firstAired;
                String Description = episode.overview;
                Double Rating = episode.siteRating;
                PreparedStatement ps = Main.db.prepareStatement("INSERT INTO Media (Title, Number, MediaType, Description, Rating, ReleaseDate) VALUES(?,?,?,?,?,?)");
                ps.setString(1, Title);
                ps.setString(2, String.valueOf(Number));
                ps.setString(3, "Episode");
                ps.setString(4, Description);
                ps.setString(5, String.valueOf(Rating));
                ps.setString(6, ReleaseDate);
                ps.executeUpdate();
            }



    }
}
