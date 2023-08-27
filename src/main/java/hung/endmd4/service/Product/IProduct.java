package hung.endmd4.service.Product;

import java.util.List;

public interface IProduct<T, E> {
    void addProduct(E e);

    void updateProduct(E e);

    void deleteProduct(T t);

    E getProductById(T t);

    List<E> getAllProducts();
}
