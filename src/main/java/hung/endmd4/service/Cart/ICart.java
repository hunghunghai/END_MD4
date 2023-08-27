package hung.endmd4.service.Cart;

import hung.endmd4.model.Cart;

import java.util.List;

public interface ICart<T, E> {
    void addToCart(E e, T u, T c, T cl, T q);

    List<Cart> getAllCart(T t);

    List<Cart> GetCartUserById(T t);

    void deleteCartById(T t);
}
