package hung.endmd4.service.Color;


import hung.endmd4.model.Color;
import hung.endmd4.util.ConnectDB;
import org.springframework.stereotype.Service;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@Service
public class ColorIMPL implements IColor {
    @Override
    public List<Color> getAllColors() {
        List<Color> colorList = new ArrayList<>();
        Connection connection = null;
        try {
            connection = ConnectDB.getConnection();
            CallableStatement callSt = connection.prepareCall("{call PROC_GetAllColor}");
            ResultSet rs = callSt.executeQuery();
            while (rs.next()) {
                Color color = new Color();
                color.setColorId(rs.getInt("color_id"));
                color.setColorName(rs.getString("color_name"));
                // Thêm các thuộc tính khác của color vào đây (nếu có)
                colorList.add(color);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ConnectDB.closeConnection();
        }
        return colorList;
    }

    @Override
    public Color getColorById(int colorId) {
        Connection connection = null;
        Color color = null;
        try {
            connection = ConnectDB.getConnection();
            CallableStatement callSt = connection.prepareCall("{call PROC_GetColorById(?)}");
            callSt.setInt(1, colorId);
            ResultSet rs = callSt.executeQuery();
            if (rs.next()) {
                color = new Color();
                color.setColorId(rs.getInt("color_id"));
                color.setColorName(rs.getString("color_name"));
                // Thêm các thuộc tính khác của color vào đây (nếu có)
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ConnectDB.closeConnection();
        }
        return color;
    }

    public void addColor(Color color) {
        Connection connection = null;
        try {
            connection = ConnectDB.getConnection();
            CallableStatement callst = connection.prepareCall("{CALL PROC_AddColor(?)}");
            callst.setString(1, color.getColorName());

            callst.executeUpdate();

            System.out.println("thêm màu thành công.");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ConnectDB.closeConnection();
        }
    }

    public void deleteColor(int colorId) {
        Connection connection = null;
        try {
            connection = ConnectDB.getConnection();
            CallableStatement callSt = connection.prepareCall("{call PROC_DeleteColor(?)}");
            callSt.setInt(1, colorId);
            callSt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ConnectDB.closeConnection();
        }
    }

}
