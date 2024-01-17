package com.pharmacystore.daoimpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.pharmacystore.connection.DbConnection;
import com.pharmacystore.dao.UserDao;
import com.pharmacystore.pojo.Order;
import com.pharmacystore.pojo.User;

public class UserDaoImpl implements UserDao {
	@Override
	public boolean register(User user) {
		try (Connection con = 
				DbConnection.getDatabaseConnection())
		{
			PreparedStatement pst = 
		con.prepareStatement("INSERT INTO user"
				+ " VALUES(?,?,?,?,?,?,?,?)");
			
			pst.setString(1,user.getUserid());
			pst.setString(2,user.getPassword());
			pst.setString(3,user.getEmailid());
			pst.setInt(4,user.getAge());
			pst.setString(5,user.getContact());
			pst.setString(6,user.getCity());
			pst.setString(7,user.getState());
			pst.setString(8,user.getPincode());
			
			int count = pst.executeUpdate();
			
			if(count > 0)
				return true;
			else
				return false;
		}
		catch(SQLException | NullPointerException exc) {
			exc.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean checkUser(User user) {
		try (Connection con = 
				DbConnection.getDatabaseConnection())
		{
			PreparedStatement pst = 
		con.prepareStatement("SELECT * FROM user"
				+ " WHERE userid = ? AND"
				+ " password = ?");
			
			pst.setString(1,user.getUserid());
			pst.setString(2,user.getPassword());
			
			ResultSet rs = pst.executeQuery();
			
			if(rs.isBeforeFirst())
				return true;
			else
				return false;
		}
		catch(SQLException | NullPointerException exc) {
			exc.printStackTrace();
			return false;
		}
	}
	public boolean cancelOrder(Order order) {
	    // Check if the order can be canceled based on its current status
	    if ("pending".equals(order.getStatus())) {
	        Connection connection = null;
	        PreparedStatement updateStatement = null;

	        try {
	            // Establish a database connection (you need to configure this part)
	            connection = DbConnection.getDatabaseConnection();

	            // Define an SQL query to update the order status to "cancelled"
	            String updateQuery = "UPDATE orders SET status = 'cancelled' WHERE order_id = ?";

	            // Prepare the SQL statement
	            updateStatement = connection.prepareStatement(updateQuery);
	            updateStatement.setInt(1, order.getOrderid()); // Assuming you have a getOrderId() method

	            // Execute the SQL statement
	            int rowsUpdated = updateStatement.executeUpdate();

	            if (rowsUpdated > 0) {
	                // Order cancellation was successful
	                // You can also perform other operations like sending notifications
	                return true;
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	            // Handle database-related errors
	        } finally {
	            // Close resources in a finally block
	            try {
	                if (updateStatement != null) {
	                    updateStatement.close();
	                }
	                if (connection != null) {
	                    connection.close();
	                }
	            } catch (SQLException e) {
	                e.printStackTrace();
	                // Handle resource closing errors
	            }
	        }
	    }
	    
	    // Order cancellation failed
	    return false;
	}
}
