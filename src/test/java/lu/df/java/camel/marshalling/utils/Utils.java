package lu.df.java.camel.marshalling.utils;


public class Utils {

    public static Object warmUp(){
        int result = 0;
        for(int i = 0; i < 1000000; i++){
            result += i;
        }
        result = result / 100;
        return result;
    }
}
