package controllers;

import org.glassfish.jersey.media.multipart.FormDataParam;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import server.Main;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.UUID;


@Path("users/")
@Consumes(MediaType.MULTIPART_FORM_DATA)
@Produces(MediaType.APPLICATION_JSON)

public class Users{
    @GET
    @Path("list")
    public String UsersList() {
        System.out.println("Invoked Users.UsersList()");
        JSONArray response = new JSONArray();
        try {
            PreparedStatement ps = Main.db.prepareStatement("SELECT UserID, Username FROM User");
            ResultSet results = ps.executeQuery();
            while (results.next()==true) {
                JSONObject row = new JSONObject();
                row.put("UserID", results.getInt(1));
                row.put("UserName", results.getString(2));
                response.add(row);
            }
            return response.toString();
        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
            return "{\"Error\": \"Unable to list items.  Error code xx.\"}";
        }
    }

    @POST
    @Path("AttemptLogin")
    public String AttemptLogin(@FormDataParam("Username") String Username, @FormDataParam("Password") String Password) {
        System.out.println("Started AttemptLogin() ond API Path User/AttemptLogin");
        try{
            PreparedStatement ps = Main.db.prepareStatement("SELECT Password FROM Users WHERE Username = ?");
            ps.setString(1, Username);
            ResultSet results = ps.executeQuery();
            if (results.next() == true) {
                String CorrectPassword = results.getString(1);
                if (generateHash(Password).equals(CorrectPassword)) {
                    String Token = UUID.randomUUID().toString();
                    PreparedStatement ps1 = Main.db.prepareStatement("UPDATE Users SET Token = ? WHERE Username = ?");
                    ps1.setString(1, Token);
                    ps1.setString(2, Username);
                    ps1.executeUpdate();
                    JSONObject userDetails = new JSONObject();
                    userDetails.put("Username", Username);
                    userDetails.put("Token", Token);
                    return userDetails.toString();
                } else {
                    return "{\"Error\": \"Incorrect Password!\"}";
                }
            } else {
                return "{\"Error\": \"Username and password are incorrect.\"}";
            }
        } catch (Exception e) {
            System.out.println("Database Error during /user/AttemptLogin: " +  e.getMessage());
            return "{\"Error\": \"Server side error!\"}";
        }
    }
    public static boolean ValidToken(String Token) {
        try{
            PreparedStatement ps = Main.db.prepareStatement("SELECT UserID FROM Users WHERE Token = ?");
            ps.setString(1, Token);
            ResultSet logoutResults = ps.executeQuery();
            return logoutResults.next();
        } catch(Exception e) {
            System.out.println("Database error" + e.getMessage());
            return false;
        }
    }
    public static String generateHash(String text) {
        try {
            MessageDigest hasher = MessageDigest.getInstance("MD5");
            hasher.update(text.getBytes());
            return DatatypeConverter.printHexBinary(hasher.digest()).toUpperCase();
        } catch (NoSuchAlgorithmException nsae) {
            return nsae.getMessage();
        }
    }
}
