package com.umcspro.eeg.client;

import com.umcspro.eeg.databasecreator.Creator;
import com.umcspro.eeg.server.EEGServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import java.sql.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ClientTest {
    private static EEGServer eegServer;
    private static Creator creator = new Creator();
    private static final String URL = "jdbc:sqlite:C:\\Users\\luke\\Documents\\usereegtest.db";
    @BeforeAll
    public static void setUp() {
        creator.create(URL);
        eegServer = new EEGServer();
        eegServer.setURL(URL);
        new Thread(() -> eegServer.start(2345)).start();
    }

    @AfterAll
    public static void stop() {
        eegServer.stop();
        creator.delete(URL);
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/test.csv", numLinesToSkip = 1)
    public void clientTest(String username,String filepath,int electrode,String image) throws InterruptedException {
        Client client = new Client();
        client.sendData(username,filepath);
        String realImage = getImage(username,electrode);
        assertEquals(image, realImage);
    }

    public String getImage(String username,int electrode ){
        String image= null;
        String sql = "SELECT image FROM user_eeg WHERE username = ? AND electrode_number = ?";

        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            pstmt.setInt(2, electrode);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    image = rs.getString("image");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return image;
    }
}
