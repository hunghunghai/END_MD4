package hung.endmd4.controller;

import hung.endmd4.model.*;
import hung.endmd4.service.catrgories.CategoryIMPL;
import hung.endmd4.service.Color.ColorIMPL;
import hung.endmd4.service.Product.ProductIMPL;
import hung.endmd4.service.size.SizeIMPL;
import hung.endmd4.service.Users.UserIMPL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/")
@ComponentScan("hung.endmd4")
public class AdminProductController {

    @Autowired
    UserIMPL userIMPL;
    @Autowired
    CategoryIMPL categoryIMPL;
    @Autowired
    SizeIMPL sizeIMPL;
    @Autowired
    ColorIMPL colorIMPL;
    @Autowired
    ProductIMPL productIMPL;

    @GetMapping("/admin")
    public String getHomeAdmin(HttpSession session) {
        // Kiểm tra xem session đã có thuộc tính "userLogins" chưa
        Boolean userLoggedIn = (Boolean) session.getAttribute("userLogins");
        String roleId = (String) session.getAttribute("userRole");

        if (userLoggedIn != null && userLoggedIn && roleId.equals("admin")) {
            // Người dùng đã đăng nhập và có vai trò của người quản trị, cho phép truy cập vào trang quản trị
            return "admin/index";
        } else {
            return "redirect:/home";
        }
    }

    @GetMapping("/admin/product")
    public String getProduct(Model model) {
        List<Product> product = productIMPL.getAllProducts();
        model.addAttribute("product", product);
        return "admin/product";
    }

    @GetMapping("/admin/category")
    public String getCategory(Model model) {
        List<Size> sizes = sizeIMPL.getAllSizes(); // Đây là ví dụ, bạn cần thay đổi phương thức tương ứng của service để lấy danh sách size
        List<Color> colors = colorIMPL.getAllColors(); // Đây là ví dụ, bạn cần thay đổi phương thức tương ứng của service để lấy danh sách màu
        List<Category> categories = categoryIMPL.getAllCategories();

        model.addAttribute("sizes", sizes);
        model.addAttribute("colors", colors);
        model.addAttribute("categories", categories);

        model.addAttribute("newCategory", new Category());
        model.addAttribute("newColor", new Color());
        model.addAttribute("newSize", new Size());

        return "admin/category";
    }

    @PostMapping("/admin/create/newCategory")
    public String createNewCategory(@ModelAttribute Category category) {
        categoryIMPL.addCategory(category);
        return "redirect:/admin/category"; // Chuyển hướng sau khi thêm category thành công
    }

    @PostMapping("/admin/create/newColor")
    public String createNewColor(@ModelAttribute Color color) {
        colorIMPL.addColor(color);
        return "redirect:/admin/category"; // Chuyển hướng sau khi thêm category thành công
    }

    @PostMapping("/admin/create/newSize")
    public String createNewSize(@ModelAttribute Size size) {
        sizeIMPL.addSize(size);
        return "redirect:/admin/category"; // Chuyển hướng sau khi thêm category thành công
    }

    @PostMapping("/admin/categories/delete/{categoryId}")
    public String deleteCategory(@PathVariable int categoryId) {
        categoryIMPL.deleteCategory(categoryId);
        return "redirect:/admin/category"; // Chuyển hướng sau khi xóa category thành công
    }

    @PostMapping("/admin/colors/delete/{colorId}")
    public String deleteColor(@PathVariable int colorId) {
        colorIMPL.deleteColor(colorId);
        return "redirect:/admin/category"; // Chuyển hướng sau khi xóa color thành công
    }

    @PostMapping("/admin/sizes/delete/{sizeId}")
    public String deleteSize(@PathVariable int sizeId) {
        sizeIMPL.deleteSize(sizeId);
        return "redirect:/admin/category"; // Chuyển hướng sau khi xóa size thành công
    }

    @GetMapping("/admin/create/products")
    public ModelAndView getCreateProduct(Model model) {
        List<Category> categories = categoryIMPL.getAllCategories();
        model.addAttribute("category", categories);
        return new ModelAndView("admin/create", "new_product", new Product());
    }

    @PostMapping("/admin/addProduct")
    public String addProduct(@ModelAttribute("new_product") ProductDao newProduct, @RequestParam("file") MultipartFile files) {
        String uploadPath = "C:\\Users\\Admin\\Desktop\\END-MD4\\src\\main\\webapp\\imgUpload\\";

        File file = new File(uploadPath);
        if (!file.exists()) {
            file.mkdirs();
        }
        String fileName = files.getOriginalFilename();

        try {
            FileCopyUtils.copy(files.getBytes(), new File(uploadPath + fileName));
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        Product p = new Product();
        p.setCategoryId(newProduct.getCategoryId());
        p.setName(newProduct.getName());
        p.setDescription(newProduct.getDescription());
        p.setUnitPrice(newProduct.getUnitPrice());
        p.setImage(fileName);
        p.setStock(newProduct.getStock());
        p.setLinkID(newProduct.getLinkID());
        p.setStatus(newProduct.getStatus());
        productIMPL.addProduct(p);

        return "redirect:/admin/product";
    }

    @GetMapping("/admin/edit/{id}")
    public String getEditProduct(@PathVariable("id") int id, Model model) {
        Product product = productIMPL.getProductById(id);
        model.addAttribute("productEdit", product);
        return "admin/edit"; // Điều hướng đến trang cập nhật thông tin sản phẩm
    }

    @PostMapping("/admin/update/product/{id}")
    public String updateProduct(@ModelAttribute("productEdit") Product productEdit, @RequestParam("file") MultipartFile files, @PathVariable("id") int id) {
        String uploadPath = "C:\\Users\\Admin\\Desktop\\END-MD4\\src\\main\\webapp\\imgUpload\\";

        File file = new File(uploadPath);
        if (!file.exists()) {
            file.mkdirs();
        }
        String fileName = files.getOriginalFilename();

        try {
            FileCopyUtils.copy(files.getBytes(), new File(uploadPath + fileName));
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        // Tìm sản phẩm hiện có trong cơ sở dữ liệu
        Product existingProduct = productIMPL.getProductById(id);
        String oldFileName = existingProduct.getImage();
        // Tìm sản phẩm hiện có trong cơ sở dữ liệu
        Product p = new Product();
        p.setProductId(id);
        p.setName(productEdit.getName());
        p.setDescription(productEdit.getDescription());
        p.setUnitPrice(productEdit.getUnitPrice());
        p.setImage(fileName);
        p.setStock(productEdit.getStock());
        p.setLinkID(productEdit.getLinkID());
        productIMPL.updateProduct(p);

        // Xóa tệp ảnh cũ sau khi đã cập nhật thành công
        if (oldFileName != null) {
            File oldFile = new File(uploadPath + oldFileName);
            if (oldFile.exists()) {
                oldFile.delete();
            }
        }

        return "redirect:/admin/product";// Chuyển hướng về trang danh sách sản phẩm sau khi cập nhật
    }

    @GetMapping("/admin/delete/product/{id}")
    public String deleteProduct(@PathVariable("id") int id) {
        productIMPL.deleteProduct(id);
        return "redirect:/admin/product";
    }
}
