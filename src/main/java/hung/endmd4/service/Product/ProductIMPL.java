package hung.endmd4.service.Product;

import hung.endmd4.model.Product;
import hung.endmd4.util.ConnectDB;
import org.springframework.stereotype.Service;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProductIMPL implements IProduct<Integer, Product> {


    //    thêm sản phẩm
    @Override
    public void addProduct(Product product) {
        Connection connection = null;
        try {
            connection = ConnectDB.getConnection();
            CallableStatement callst = connection.prepareCall("{CALL PROC_AddNewProduct(?, ?, ?, ?, ?, ?)}");
            callst.setString(1, product.getName());
            callst.setString(2, product.getDescription());
            callst.setDouble(3, product.getUnitPrice());
            callst.setString(4, product.getImage());
            callst.setInt(5, product.getStock());
            callst.setInt(6, product.getCategoryId());

            callst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ConnectDB.closeConnection();
        }
    }


    //    sửa sản phẩm
    @Override
    public void updateProduct(Product product) {
        Connection connection = null;
        try {
            connection = ConnectDB.getConnection();
            CallableStatement callableStatement = connection.prepareCall("{call PROC_UpdateProduct(?,?,?,?,?,?)}");
            callableStatement.setInt(1, product.getProductId());
            callableStatement.setString(2, product.getName());
            callableStatement.setString(4, product.getDescription());
            callableStatement.setDouble(3, product.getUnitPrice());
            callableStatement.setString(5, product.getImage());
            callableStatement.setInt(6, product.getStock());

            callableStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ConnectDB.closeConnection();
        }
    }

    //    xóa sản phẩm
    @Override
    public void deleteProduct(Integer productId) {
        Connection connection = null;
        try {
            connection = ConnectDB.getConnection();
            CallableStatement callSt = connection.prepareCall("{call PROC_DeleteProduct(?)}");
            callSt.setInt(1, productId);
            callSt.executeQuery();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ConnectDB.closeConnection();
        }
    }

    //    lấy sản phẩm theo id
    @Override
    public Product getProductById(Integer productId) {
        Connection connection = null;
        Product product = null;
        try {
            connection = ConnectDB.getConnection();
            CallableStatement callSt = connection.prepareCall("{call PROC_FindById(?)}");
            callSt.setInt(1, productId);
            ResultSet rs = callSt.executeQuery();
            if (rs.next()) {
                product = new Product();
                product.setProductId(rs.getInt("product_id"));
                product.setName(rs.getString("name"));
                product.setDescription(rs.getString("description"));
                product.setUnitPrice(rs.getFloat("unit_price"));
                product.setImage(rs.getString("image"));
                product.setStock(rs.getInt("stock"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ConnectDB.closeConnection();
        }
        return product;
    }

    //    lấy toàn bộ sản phẩm
    @Override
    public List<Product> getAllProducts() {
        List<Product> productList = new ArrayList<>();
        Connection connection = null;
        try {
            connection = ConnectDB.getConnection();
            CallableStatement callSt = connection.prepareCall("{call PROC_FindAllProducts()}");
            ResultSet rs = callSt.executeQuery();
            while (rs.next()) {
                Product product = new Product();
                product.setProductId(rs.getInt("product_id"));
                product.setName(rs.getString("name"));
                product.setDescription(rs.getString("description"));
                product.setUnitPrice(rs.getFloat("unit_price"));
                product.setImage(rs.getString("image"));
                product.setStock(rs.getInt("stock"));
                productList.add(product);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ConnectDB.closeConnection();
        }
        return productList;
    }
}
