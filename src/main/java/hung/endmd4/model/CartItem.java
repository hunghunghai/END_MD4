package hung.endmd4.model;

public class CartItem {

    private int cartId;
    private int userId;
    private String productname;
    private String ImageProduct;
    private Double price;
    private String color;
    private String size;
    private int quantity;

    public CartItem() {
    }

    public CartItem(int cartId, int userId, String productname, String imageProduct, Double price, String color, String size, int quantity) {
        this.cartId = cartId;
        this.productname = productname;
        this.ImageProduct = imageProduct;
        this.price = price;
        this.color = color;
        this.size = size;
        this.quantity = quantity;
        this.userId = userId;
    }

    public String getProductname() {
        return productname;
    }

    public void setProductname(String productname) {
        this.productname = productname;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getImageProduct() {
        return ImageProduct;
    }

    public void setImageProduct(String imageProduct) {
        ImageProduct = imageProduct;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getCartId() {
        return cartId;
    }

    public void setCartId(int cartId) {
        this.cartId = cartId;
    }
}
