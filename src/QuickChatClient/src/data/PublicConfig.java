package data;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;

public class PublicConfig implements Serializable {

    private static final long serialVersionUID = 1000L;

    public class ServerInfo implements Serializable{
        String Host;
        int Port;
    }

    private ArrayList<ServerInfo> serverInfoList;

    /** */
    private ArrayList<String> idList;
}
