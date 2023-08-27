package hung.endmd4.service.catrgories;

import hung.endmd4.model.Category;

import java.util.List;

public interface ICategory {
    List<Category> getAllCategories();

    Category getCategoryById(int categoryId);
}
