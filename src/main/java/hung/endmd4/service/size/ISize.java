package hung.endmd4.service.size;

import hung.endmd4.model.Size;

import java.util.List;

public interface ISize {
    List<Size> getAllSizes();

    Size getSizeById(int sizeId);
}
