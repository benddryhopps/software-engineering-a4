package software_engineering;

import java.sql.*;

public class BusCRUDActions{

    private Database db=new Database();

    public boolean updateCapacity(
            String busID,
            int newCapacity,
            int oldCapacity) {
        if(newCapacity > oldCapacity) {
            System.out.println("Capacity cannot increase.");
            return false;
        }

        String sql =
                "UPDATE busRepo SET capacity=? WHERE busID=?";
        try(
                Connection conn=db.connect();
                PreparedStatement ps=conn.prepareStatement(sql)
        ){
            ps.setInt(1, newCapacity);
            ps.setString(2, busID);
            ps.executeUpdate();
            return true;
        } catch(Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
