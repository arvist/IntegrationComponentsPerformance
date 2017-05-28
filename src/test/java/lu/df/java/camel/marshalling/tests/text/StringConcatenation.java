package lu.df.java.camel.marshalling.tests.text;


import lu.df.java.camel.marshalling.utils.Utils;
import org.junit.Test;

public class StringConcatenation {


    @Test
    public void testStringPerformance(){

        Utils.warmUp();

        runBothConcatMethods(100);
        runBothConcatMethods(250);
        runBothConcatMethods(500);
        runBothConcatMethods(1000);
        runBothConcatMethods(2500);
        runBothConcatMethods(4000);

    }

    private static void runBothConcatMethods(int size){
        concatStringBuilder(size);
        concatString(size);
    }


    private static void concatStringBuilder(int size) {
        StringBuilder s = new StringBuilder();
        long start = System.nanoTime();
        for(int i=0; i < size; i++) {
            s = s.append(String.valueOf(i));
        }
        long end = System.nanoTime();
        System.out.println("Concat of " + size + " strings with StringBuilder.class took " + (end-start) + " nano seconds.");
    }

    private static void concatString(int size) {

        String s = "";
        long start = System.nanoTime();
        for(int i=0; i < size; i++){
            s += (String.valueOf(i));
        }
        long end = System.nanoTime();
        System.out.println("Concat of " + size + " strings with String.class took " + (end-start) + " nano seconds.");
    }
}
