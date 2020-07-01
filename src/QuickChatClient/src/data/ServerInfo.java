package data;

import java.io.Serializable;

public class ServerInfo implements Serializable {

    //private static final long serialVersionUID = 1L;

    private String Host;

    private int Port;

    public ServerInfo(String host,int port){
        Host=host;
        Port=port;
    }

    public String toString(){
        return Host + ", " + Port;
    }
}