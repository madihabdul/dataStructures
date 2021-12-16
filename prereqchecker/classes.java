package prereqchecker;
import java.util.*;

public class classes {
    
    private String classTitle;
    private ArrayList<String> needToTake = new ArrayList<String>();
    private ArrayList<Integer> needIntToTake = new ArrayList<Integer>();

    //course itself + reqs needed
    public void Classes (String classTitle) {
        this.classTitle = classTitle;
        this.needToTake = new ArrayList<String>();
        this.needIntToTake = new ArrayList<Integer>();
    }

    public String toString() {
        return classTitle;
    }

    // get the course title
    public String getName() {
        return classTitle; // or this.classTitle?
    }

    // add name to arrayList
    public void addName(String className) {
        this.needToTake.add(className);
    }

    // return arraylist of needed pre reqs
    public ArrayList<String> getPreReqs() {
        return this.needToTake;
    }

    // return array list of int needed pre reqs 
    public ArrayList<Integer> getIntPreReqs() {
        return this.needIntToTake;
    }

    // add requirements
    public void takePrePreq(String preReq) {
        this.needToTake.add(preReq);
    }

    // add int pre req requirements
    public void takeIntPreReq(int preReq) {
        this.needIntToTake.add(preReq);
    }
}
