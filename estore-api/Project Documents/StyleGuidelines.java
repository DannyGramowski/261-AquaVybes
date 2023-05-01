/**
 * style guidelins for the 261 project.
 * @version 1
 * @author Danny Gramowski
 */

/**
 * this is a representation of the agreed upon style for the project
 */
public class StyleGuidelines {
    final double FUN_PI = 3.1415;

    int num;
    String aName;

    /**
     * creates a new StleGuidelines
     * @param num a number
     * @param aName the name
     */
    public StyleGuidelines(int num, String aName) {
        this.num = num;
        this.aName = aName;
    }

    /**
     * prints out some stuff
     * @return 1
     */
    public int getRandom() {
        boolean aBool = true;
        var a = 5; //dont use this too much

        if(aBool) { //this is a comment
            System.out.println("true");
        }

        for(int i = 0; i < 10; i++) {
            System.out.println(i);
        }

        return 1;
    }

    /**
     * @return ahhhhh
     */
    @Override
    public String toString() {
        return "ahhhh";
    }
}
