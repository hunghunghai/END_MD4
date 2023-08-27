package hung.endmd4.controller;

import hung.endmd4.model.Users;
import hung.endmd4.service.Users.UserIMPL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/")
@ComponentScan("hung.endmd4")
public class AdminUserController {
    @Autowired
    UserIMPL userIMPL;

    @GetMapping("/admin/user")
    public ModelAndView getUser() {
        List<Users> users = userIMPL.getAllUsers();
        return new ModelAndView("admin/user", "listUser", users);
    }

    @GetMapping("/admin/LockedUsers/{id}")
    public String getLockedUsers(@PathVariable("id") int id) {
        userIMPL.blockUser(id);
        return "redirect:/admin/user";
    }

    @GetMapping("/admin/UnLockedUsers/{id}")
    public String getUnLockedUsers(@PathVariable("id") int id) {
        userIMPL.UnLockUser(id);
        return "redirect:/admin/user";
    }

}
