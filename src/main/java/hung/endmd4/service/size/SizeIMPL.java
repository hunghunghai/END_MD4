package hung.endmd4.service.size;

import hung.endmd4.model.Size;
import hung.endmd4.service.size.ISize;
import hung.endmd4.util.ConnectDB;
import org.springframework.stereotype.Service;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@Service
public class SizeIMPL implements ISize {
    @Override
    public List<Size> getAllSizes() {
        List<Size> sizeList = new ArrayList<>();
        Connection connection = null;
        try {
            connection = ConnectDB.getConnection();
            CallableStatement callSt = connection.prepareCall("{call PROC_GetAllSize}");
            ResultSet rs = callSt.executeQuery();
            while (rs.next()) {
                Size size = new Size();
                size.setSizeId(rs.getInt("size_id"));
                size.setSizeName(rs.getString("size_name"));
                // Thêm các thuộc tính khác của size vào đây (nếu có)
                sizeList.add(size);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ConnectDB.closeConnection();
        }
        return sizeList;
    }

    @Override
    public Size getSizeById(int sizeId) {
        Connection connection = null;
        Size size = null;
        try {
            connection = ConnectDB.getConnection();
            CallableStatement callSt = connection.prepareCall("{call PROC_GetSizeById(?)}");
            callSt.setInt(1, sizeId);
            ResultSet rs = callSt.executeQuery();
            if (rs.next()) {
                size = new Size();
                size.setSizeId(rs.getInt("size_id"));
                size.setSizeName(rs.getString("size_name"));
                // Thêm các thuộc tính khác của size vào đây (nếu có)
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ConnectDB.closeConnection();
        }
        return size;
    }

    public void addSize(Size size) {
        Connection connection = null;
        try {
            connection = ConnectDB.getConnection();
            CallableStatement callst = connection.prepareCall("{CALL PROC_AddSize(?)}");
            callst.setString(1, size.getSizeName());

            callst.executeUpdate();

            System.out.println("thêm kích cỡ thành công.");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ConnectDB.closeConnection();
        }
    }

    public void deleteSize(int sizeId) {
        Connection connection = null;
        try {
            connection = ConnectDB.getConnection();
            CallableStatement callSt = connection.prepareCall("{call PROC_DeleteSize(?)}");
            callSt.setInt(1, sizeId);
            callSt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ConnectDB.closeConnection();
        }
    }
}
