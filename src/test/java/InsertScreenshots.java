
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;
import org.junit.Before;
import org.junit.Test;
import org.postgresql.Driver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author andrey
 */
public class InsertScreenshots {
    Logger log = LoggerFactory.getLogger("InsertScreenshots");

    String userName = "trdr";
    String password = "trdr";

    @Before
    public void tryInsertScreenshots(){
        try{
            insertScreenshots();
        }
        catch(Exception ex){
            System.out.println(ex);
            log.info("Error while loading screenshots. It's common situation, it's may already exists",ex);
        }
    }
    
    public void insertScreenshots() throws FileNotFoundException, SQLException, IOException {
        Connection conn = this.getConnection();
        loadFile("/home/andrey/Downloads/staticRes/screen1.jpg", conn,1L,1L);
        loadFile("/home/andrey/Downloads/staticRes/screen2.jpg", conn,2L,1L);
        loadFile("/home/andrey/Downloads/staticRes/screen3.jpg", conn,3L,1L);
        conn.commit();
        conn.close();
    }
    
    @Test
    public void emptyTest(){
        
    }
    
    private void loadFile(String filePath, Connection conn, Long id, Long dealId) throws FileNotFoundException, SQLException, IOException {
        File file = new File(filePath);
        FileInputStream fis = new FileInputStream(file);
        PreparedStatement ps = conn.prepareStatement("INSERT INTO trdr.screenshots(\n"
                + "            screenshot_id, file, file_name, deal_id)\n"
                + "    VALUES (?, ?, ?, ?);");
        ps.setLong(1, id);
        ps.setBinaryStream(2, fis,fis.available());
        ps.setString(3, file.getName());
        ps.setLong(4, dealId);
        ps.executeUpdate();
        ps.close();
        fis.close();
    }

    public Connection getConnection() throws SQLException {
        Connection conn = null;
        Properties connectionProps = new Properties();
        connectionProps.put("user", this.userName);
        connectionProps.put("password", this.password);
        DriverManager.registerDriver(new Driver());
        conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/trdr", connectionProps);
        System.out.println("Connected to database");
        return conn;
    }
}
