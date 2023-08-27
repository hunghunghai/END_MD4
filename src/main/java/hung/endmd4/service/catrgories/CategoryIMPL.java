package hung.endmd4.service.catrgories;

import hung.endmd4.model.Category;
import hung.endmd4.util.ConnectDB;
import org.springframework.stereotype.Service;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryIMPL implements ICategory {
    @Override
    public List<Category> getAllCategories() {
        List<Category> categories = new ArrayList<>();
        Connection connection = null;
        try {
            connection = ConnectDB.getConnection();
            String query = "SELECT * FROM categories";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                Category category = new Category();
                category.setCategoryid(rs.getInt("category_id"));
                category.setCategoryname(rs.getString("category_name"));
                categories.add(category);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ConnectDB.closeConnection();
        }
        return categories;
    }

    @Override
    public Category getCategoryById(int categoryId) {
        return null;
    }

    public void addCategory(Category category) {
        Connection connection = null;
        try {
            connection = ConnectDB.getConnection();
            CallableStatement callst = connection.prepareCall("{CALL PROC_AddCategory(?)}");
            callst.setString(1, category.getCategoryname());

            callst.executeUpdate();

            System.out.println("thêm danh mục thành công.");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ConnectDB.closeConnection();
        }
    }

    public void deleteCategory(int categoryId) {
        Connection connection = null;
        try {
            connection = ConnectDB.getConnection();
            CallableStatement callSt = connection.prepareCall("{call PROC_DeleteCategory(?)}");
            callSt.setInt(1, categoryId);
            callSt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ConnectDB.closeConnection();
        }
    }
}
