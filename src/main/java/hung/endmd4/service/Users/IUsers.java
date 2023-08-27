package hung.endmd4.service.Users;

import java.util.List;

public interface IUsers<T, E> {
    void addUser(T user);

    void updateUser(T user);

    void deleteUser(E id);

    T getUserById(E id);

    List<T> getAllUsers();
}
