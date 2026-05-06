package dao;
import modele.*;
import java.util.List;

public interface PlatDAO  extends IDAO<Plat> {
    List<Plat> findByMenu(int menuId);
}


