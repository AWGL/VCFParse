/**
 * Created by Sara on 07-Nov-16.
 */

//import java.io.*;

public class Main {

    public static void main(String[] args) {
        System.out.println("Hello World");

        //An instance of the class needs to be created before the non-static methods can be referenced
        //Instance of the TestClass, which I have called obj
        TestClass obj = new TestClass ();

        //calling the parSer method of the TestClass within the obj instance of the class
        obj.parSer();


    }
}
