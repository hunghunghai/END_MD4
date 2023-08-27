package hung.endmd4.model;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class ProductDao {
    private int productId;
    private int categoryId;
    private String name;
    private String description;
    private double unitPrice;
    private MultipartFile image;
    private int stock;
    private Boolean status;
    private int linkID;

    // Constructor

    public ProductDao() {
    }

    public ProductDao(int productId, int categoryId, String name, String description, double unitPrice, MultipartFile image, int stock, Boolean status, int linkID) {
        this.productId = productId;
        this.categoryId = categoryId;
        this.name = name;
        this.description = description;
        this.unitPrice = unitPrice;
        this.image = image;
        this.stock = stock;
        this.status = true;
        this.linkID = linkID;
    }

    // Getters and Setters


    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public MultipartFile getImage() {
        return image;
    }

    public void setImage(MultipartFile image) {
        this.image = image;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public int getLinkID() {
        return linkID;
    }

    public void setLinkID(int linkID) {
        this.linkID = linkID;
    }
}
