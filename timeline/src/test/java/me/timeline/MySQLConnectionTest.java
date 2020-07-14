package me.timeline;
import java.sql.Connection;
import java.sql.DriverManager;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MySQLConnectionTest {
	@Test
	void contextLoads() {
	}
	
	@Test
	public void testConnection() throws Exception{
		String driver = "com.mysql.cj.jdbc.Driver";
		String url = "jdbc:mysql://localhost:3306/Timeline?serverTimezone=UTC";
		String user = "root";
		String password = "";
		
		Class.forName(driver);
		Connection con = DriverManager.getConnection(url, user, password);
		System.out.println("con: "+ con);
		con.close();
	}
}
