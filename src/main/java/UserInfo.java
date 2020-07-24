import java.io.Serializable;
import java.util.ArrayList;

public class UserInfo implements Serializable {

    static final long serialVersionUID = 0L;

    ArrayList<String> cities;
    boolean newCityFlag;
    boolean removeCityFlag;
    long id;

    public UserInfo(ArrayList<String> cities, boolean newCityFlag, boolean removeCityFlag, int id) {
        this.cities = cities;
        this.newCityFlag = newCityFlag;
        this.removeCityFlag = removeCityFlag;
        this.id = id;
    }

    public UserInfo(long id) {
        cities = new ArrayList<>();
        newCityFlag = false;
        removeCityFlag = false;
        this.id = id;
    }
}
