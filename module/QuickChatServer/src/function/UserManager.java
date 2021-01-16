package function;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.function.Predicate;

public class UserManager {

    private static UserManager instance;

    public static UserManager getInstance() {
        if (instance == null) {
            instance = new UserManager();
        }
        return instance;
    }

    private UserManager() {
    }

    /**
     * Set user offline and remove from online list
     *
     * @param id
     */
    public void UserOffline(BigInteger id) {
        onlineList.removeIf(item -> id.compareTo(item) == 0);
    }

    /**
     * Set user online
     * @param id
     */
    public void UserOnline(BigInteger id) {
        for (var item :
                onlineList) {
            if (id.compareTo(item) == 0) {
                return;
            }
        }
        onlineList.add(id);
    }

    public ArrayList<BigInteger> getOnlineList() {
        return onlineList;
    }

    public String getOnlineListStr(){
        StringBuilder idListStr = new StringBuilder();
        for (BigInteger bigInteger :
                UserManager.getInstance().getOnlineList()) {
            idListStr.append(bigInteger);
            idListStr.append('#');
        }
        return idListStr.toString();
    }

    /**
     * The id list of online user
     */
    private ArrayList<BigInteger> onlineList = new ArrayList<>();

}
