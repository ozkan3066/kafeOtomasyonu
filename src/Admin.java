import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Admin extends User{
	


	public Admin(String id,String password, String type) {
		super(id,password, type);
	}
	public Admin() {}
	
	 
	public ArrayList<User> getGarsonArrayList() throws SQLException
	{
		ArrayList<User> list = new ArrayList<>();
		User obj;
		try {
			Connection con = conn.baglantiAl();
			Statement st = null;
			ResultSet rs = null;
			st = con.createStatement();
			rs = st.executeQuery("SELECT * FROM user WHERE type = 'garson' ");
			while(rs.next())
			{
				obj = new User(rs.getString("id"),rs.getString("password"),rs.getString("type"));
				list.add(obj);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	
	
}
