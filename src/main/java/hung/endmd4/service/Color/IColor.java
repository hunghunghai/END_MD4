package hung.endmd4.service.Color;

import hung.endmd4.model.Color;

import java.util.List;

public interface IColor {
    List<Color> getAllColors();
    Color getColorById(int colorId);
}
