package bridgelabz.utils;

import java.util.Scanner;

public class InputUtil {

    //input constant
    private final static Scanner sc = new Scanner(System.in);

    //method of int value
    public static int getIntValue()
    {
        return sc.nextInt();
    }

    //method of string value
    public static String getStringValue()
    {
        return sc.next();
    }
}
