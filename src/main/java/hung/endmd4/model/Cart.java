package hung.endmd4.model;

import java.time.LocalDateTime;
import java.util.Date;

public class Cart {
    private int cartId;
    private int userId;
    private int productsId;
    private int sizesId;
    private int colorsId;
    private int quantity;
    private LocalDateTime created;

    public Cart() {
    }

    public Cart(int cartId, int userId, int productsId, int sizesId, int colorsId, int quantity, LocalDateTime created) {
        this.cartId = cartId;
        this.userId = userId;
        this.productsId = productsId;
        this.sizesId = sizesId;
        this.colorsId = colorsId;
        this.quantity = quantity;
        this.created = created;
    }

    public int getCartId() {
        return cartId;
    }

    public void setCartId(int cartId) {
        this.cartId = cartId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getProductsId() {
        return productsId;
    }

    public void setProductsId(int productsId) {
        this.productsId = productsId;
    }

    public int getSizesId() {
        return sizesId;
    }

    public void setSizesId(int sizesId) {
        this.sizesId = sizesId;
    }

    public int getColorsId() {
        return colorsId;
    }

    public void setColorsId(int colorsId) {
        this.colorsId = colorsId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }
}
