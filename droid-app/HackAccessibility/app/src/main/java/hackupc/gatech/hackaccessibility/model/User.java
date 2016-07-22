package hackupc.gatech.hackaccessibility.model;

/**
 * Created by michael on 7/22/16.
 */
public class User {

    public static boolean isTerry = true;

    public static String GetName() {
        return isTerry ? "Terry Tester" : "Jerry Smith";
    }

}
