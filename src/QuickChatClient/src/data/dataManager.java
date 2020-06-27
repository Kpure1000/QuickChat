package data;

public class dataManager {
    private static dataManager instance;

    private  dataManager(){}

    public static dataManager getInstance(){
        if(instance==null){
            instance=new dataManager();
            return instance;
        }
        return instance;
    }

    /*------------------------------------------------------*/


}
