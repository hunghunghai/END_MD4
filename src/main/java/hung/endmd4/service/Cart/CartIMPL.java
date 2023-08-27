package hung.endmd4.service.Cart;

import hung.endmd4.model.Cart;
import hung.endmd4.model.Product;
import hung.endmd4.util.ConnectDB;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class CartIMPL implements ICart<Integer, Product> {
    @Override
    public void addToCart(Product product, Integer userId, Integer colorId, Integer sizeId, Integer quantity) {
        Connection connection = null;

        try {
            connection = ConnectDB.getConnection();
            CallableStatement callableStatement = connection.prepareCall("{CALL PROC_AddToCart(?, ?, ?, ?, ?)}");

            callableStatement.setInt(1, userId);
            callableStatement.setInt(2, product.getProductId());
            callableStatement.setInt(3, colorId);
            callableStatement.setInt(4, sizeId);
            callableStatement.setInt(5, quantity);

            callableStatement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            ConnectDB.closeConnection();
        }
    }

    @Override
    public List<Cart> getAllCart(Integer userId) {
        Connection connection = null;
        CallableStatement callableStatement = null;
        ResultSet resultSet = null;
        List<Cart> cartList = new ArrayList<>();

        try {
            connection = ConnectDB.getConnection();
            callableStatement = connection.prepareCall("{CALL PROC_GetAllCartByUserId(?)}");
            callableStatement.setInt(1, userId);
            resultSet = callableStatement.executeQuery();

            while (resultSet.next()) {
                Cart cart = new Cart();
                cart.setCartId(resultSet.getInt("cart_id"));
                cart.setUserId(resultSet.getInt("user_id"));
                cart.setProductsId(resultSet.getInt("product_id"));
                cart.setColorsId(resultSet.getInt("color_id")); // Set color_id
                cart.setSizesId(resultSet.getInt("size_id")); // Set size_id
                cart.setQuantity(resultSet.getInt("quantity"));
                cart.setCreated(resultSet.getTimestamp("created_at").toLocalDateTime()); // Set created_at

                cartList.add(cart);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            ConnectDB.closeConnection();
        }

        return cartList;
    }

    @Override
    public List<Cart> GetCartUserById(Integer cartId) {
        Connection connection = null;
        CallableStatement callableStatement = null;
        ResultSet resultSet = null;
        List<Cart> cartList = new ArrayList<>();

        try {
            connection = ConnectDB.getConnection();
            callableStatement = connection.prepareCall("{CALL PROC_GetCartById(?)}");
            callableStatement.setInt(1, cartId);
            resultSet = callableStatement.executeQuery();

            while (resultSet.next()) {
                Cart cart = new Cart();
                cart.setCartId(resultSet.getInt("cart_id"));
                cart.setUserId(resultSet.getInt("user_id"));
                cart.setProductsId(resultSet.getInt("product_id"));
                cart.setColorsId(resultSet.getInt("color_id")); // Set color_id
                cart.setSizesId(resultSet.getInt("size_id")); // Set size_id
                cart.setQuantity(resultSet.getInt("quantity"));
                cart.setCreated(resultSet.getTimestamp("created_at").toLocalDateTime()); // Set created_at

                cartList.add(cart);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            ConnectDB.closeConnection();
        }

        return cartList;
    }

    public void updateCartItemQuantity(int cartId, int quantity) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = ConnectDB.getConnection();
            String query = "UPDATE cart SET quantity = ? WHERE cart_id = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, quantity);
            preparedStatement.setInt(2, cartId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            ConnectDB.closeConnection();
        }
    }

    @Override
    public void deleteCartById(Integer integer) {

    }
}
