package hung.endmd4.controller;

import hung.endmd4.model.*;
import hung.endmd4.service.Cart.CartIMPL;
import hung.endmd4.service.Color.ColorIMPL;
import hung.endmd4.service.Product.ProductIMPL;
import hung.endmd4.service.Users.UserIMPL;
import hung.endmd4.service.catrgories.CategoryIMPL;
import hung.endmd4.service.size.SizeIMPL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/")
@ComponentScan("hung.endmd4")
public class HomeController {
    @Autowired
    ProductIMPL productIMPL;
    @Autowired
    UserIMPL userIMPL;
    @Autowired
    CartIMPL cartIMPL;
    @Autowired
    CategoryIMPL categoryIMPL;
    @Autowired
    ColorIMPL colorIMPL;
    @Autowired
    SizeIMPL sizeIMPL;

    @GetMapping({"/", "home"})
    public String getHome(Model model, HttpSession session) {
        if (session.getAttribute("usersLogin") != null) {
            Users users = (Users) session.getAttribute("usersLogin");
            List<Cart> cart = cartIMPL.getAllCart(users.getUserId());
            model.addAttribute("cartItems", cart);
        }
        List<Product> product = productIMPL.getAllProducts();
        model.addAttribute("productlist", product);
        return "index";
    }

    @GetMapping("/login")
    public String listUsers(Model model) {
        // Lấy danh sách người dùng từ Repository
        List<Users> users = userIMPL.getAllUsers();
        model.addAttribute("users", users);
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam("username") String username, @RequestParam("password") String password, Model model, HttpSession session) {
        if (username == null || username.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            model.addAttribute("loginErrorVald", "Please enter both username and password");
            return "login";
        }
        Users user = userIMPL.login(username, password); // Gọi phương thức login với tên đăng nhập và mật khẩu

        if (user == null) {
            model.addAttribute("loginError", "Username or password incorrect");
            return "login";
        } else if (user.getStatus() == 0) {
            model.addAttribute("loginError", "Account has been locked");
            return "login";
        } else {
            session.setAttribute("usersLogin", user);

            if (user.getStatus() == 1) {
                if (user.getRoleId() == 1) {
                    session.setAttribute("admin", "admin");
                    return "redirect:/admin";
                } else {
                    session.setAttribute("users", "users");
                    return "redirect:/";
                }
            }
        }
        return "login";
    }

    @GetMapping("/register")
    public ModelAndView showRegisterForm() {
        return new ModelAndView("register", "userRigister", new Users());
    }

    @PostMapping("/registerForm")
    public String registerUser(@ModelAttribute("userRigister") Users user, @RequestParam("confirm_password") String confirm_password, Model model) {
        if (user.getUsername() == null || user.getUsername().isEmpty()) {
            model.addAttribute("usernameError", "Username is required");
            return "register";
        } else if (userIMPL.isUsernameRegistered(user.getUsername())) {
            model.addAttribute("usernameError", "Username is already registered");
            return "register";
        }

        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            model.addAttribute("emailError", "Email is required");
            return "register";
        } else if (!isValidEmail(user.getEmail())) {
            model.addAttribute("emailError", "Email invalidate");
            return "register";
        } else if (userIMPL.isEmailRegistered(user.getEmail())) {
            model.addAttribute("emailError", "Email is already registered");
            return "register";
        }


        if (user.getFirstName() == null || user.getFirstName().isEmpty()) {
            model.addAttribute("firstNameError", "First Name is required");
            return "register";
        } else if (!Character.isUpperCase(user.getFirstName().charAt(0))) {
            model.addAttribute("firstNameError", "First Name must start with an uppercase letter");
            return "register";
        }

        if (user.getLastName() == null || user.getLastName().isEmpty()) {
            model.addAttribute("lastNameError", "Last Name is required");
            return "register";
        } else if (!Character.isUpperCase(user.getLastName().charAt(0))) {
            model.addAttribute("lastNameError", "Last Name must start with an uppercase letter");
            return "register";
        }

        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            model.addAttribute("passwordError", "Password is required");
            return "register";
        }

        String password = user.getPassword();
        if (password.length() < 8 || !Character.isUpperCase(password.charAt(0)) || !containsSpecialCharacter(password)) {
            model.addAttribute("passwordError", "Password starts with uppercase and must be 8 characters or more");
            return "register";
        }

        String confirmPassword = confirm_password;
        if (confirmPassword == null || confirmPassword.isEmpty() || !confirmPassword.equals(user.getPassword())) {
            model.addAttribute("confirmPasswordError", "Confirm password does not match");
            return "register";
        }

        userIMPL.addUser(user);
        return "redirect:/login";
    }


    @GetMapping("/logout")
    public String getLogout(HttpSession session) {
        session.removeAttribute("usersLogin");
        session.removeAttribute("users");
        session.removeAttribute("admin");
        return "redirect:/";
    }

    @GetMapping("/home/product")
    public String homeProduct(Model model, HttpSession session) {
        if (session.getAttribute("usersLogin") != null) {
            Users users = (Users) session.getAttribute("usersLogin");
            List<Cart> cart = cartIMPL.getAllCart(users.getUserId());
            model.addAttribute("cartItems", cart);
        }
        List<Product> product = productIMPL.getAllProducts();
        model.addAttribute("productlist", product);
        return "product";
    }

    @GetMapping("/home/shopingCart")
    public String homeShopingCart(HttpSession session, Model model) {
        if (session.getAttribute("usersLogin") != null) {
            // Lấy thông tin người dùng từ session
            Users users = (Users) session.getAttribute("usersLogin");

//            List<Cart> cartlistId = cartIMPL.get

            // Lấy danh sách giỏ hàng của người dùng
            List<Cart> cart = cartIMPL.getAllCart(users.getUserId());

            List<CartItem> cartItems = new ArrayList<>();

            // Duyệt qua danh sách giỏ hàng để lấy ra thông tin cần thiết
            for (Cart cartItem : cart) {
                CartItem carts = new CartItem();
                Product product = productIMPL.getProductById(cartItem.getProductsId());
                Color color = colorIMPL.getColorById(cartItem.getColorsId());
                Size size = sizeIMPL.getSizeById(cartItem.getSizesId());
                carts.setCartId(cartItem.getCartId());
                carts.setUserId(cartItem.getUserId());
                carts.setProductname(product.getName());
                carts.setImageProduct(product.getImage());
                carts.setPrice(product.getUnitPrice());
                carts.setColor(color.getColorName());
                carts.setSize(size.getSizeName());
                carts.setQuantity(cartItem.getQuantity());
                cartItems.add(carts);
            }

            // Đưa danh sách cartItems vào model để hiển thị trong view
            model.addAttribute("cartItems", cartItems);

            return "shopingCart";
        }

        return "shopingCart";
    }

    @PostMapping("/updateCartItemQuantity/{cartId}/{quantity}")
    @ResponseBody
    public String updateCartItemQuantity(@PathVariable("cartId") int cartId, @PathVariable("quantity") int quantity) {
        // Gọi phương thức cập nhật số lượng trong service hoặc repository
        cartIMPL.updateCartItemQuantity(cartId, quantity);
        return "success";
    }

    @GetMapping("/home/blog")
    public String homeBlog() {
        return "blog";
    }

    @GetMapping("/home/about")
    public String homeAbout(Model model, HttpSession session) {
        if (session.getAttribute("usersLogin") != null) {
            Users users = (Users) session.getAttribute("usersLogin");
            List<Cart> cart = cartIMPL.getAllCart(users.getUserId());
            model.addAttribute("cartItems", cart);
        }
        return "about";
    }

    @GetMapping("/home/contact")
    public String homeContact(Model model, HttpSession session) {
        if (session.getAttribute("usersLogin") != null) {
            Users users = (Users) session.getAttribute("usersLogin");
            List<Cart> cart = cartIMPL.getAllCart(users.getUserId());
            model.addAttribute("cartItems", cart);
        }
        return "contact";
    }

    @GetMapping("/home/product/{productId}")
    public String viewProduct(@PathVariable int productId, Model model, HttpSession session) {
        if (session.getAttribute("usersLogin") != null) {
            Users users = (Users) session.getAttribute("usersLogin");
            List<Cart> cart = cartIMPL.getAllCart(users.getUserId());
            model.addAttribute("cartItems", cart);
        }
        Product product = productIMPL.getProductById(productId);
        List<Color> color = colorIMPL.getAllColors();
        List<Size> size = sizeIMPL.getAllSizes();
        model.addAttribute("product", product);
        model.addAttribute("colorList", color);
        model.addAttribute("sizeList", size);
        return "productDetail";
    }

    @PostMapping("/addcart/{productId}")
    public String addToCart(@PathVariable("productId") int id, @RequestParam("selectedColor") int colorId, @RequestParam("selectedSize") int sizeId, @RequestParam("quantity") int quantity, HttpSession session) {
        Users users = (Users) session.getAttribute("usersLogin");
        Product product = productIMPL.getProductById(id);
        Color color = colorIMPL.getColorById(colorId);
        Size size = sizeIMPL.getSizeById(sizeId);
        cartIMPL.addToCart(product, users.getUserId(), color.getColorId(), size.getSizeId(), quantity);
        return "redirect:/home/product/" + id;
    }

    private boolean isValidEmail(String email) {
        // Regular expression for a simple email format validation
        String regex = "^[A-Za-z0-9+_.-]+@(.+)$";
        return email.matches(regex);
    }

    private boolean containsSpecialCharacter(String password) {
        String specialCharacters = "!@#$%^&*()-_=+[]{}|;:'\",.<>?";
        for (char c : specialCharacters.toCharArray()) {
            if (password.contains(String.valueOf(c))) {
                return true;
            }
        }
        return false;
    }
}
