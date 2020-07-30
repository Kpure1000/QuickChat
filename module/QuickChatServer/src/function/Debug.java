package function;

public class Debug {
    public static void Log(String msg){
        System.out.println("Log: "+msg);
    }

    public static void LogError(String error){
        System.out.println("ERROR: "+error);
    }

    public static void LogWarning(String warning){
        System.out.println("WARNING: "+warning);
    }
}
