package prereqchecker;

import java.util.*;

/**
 * Steps to implement this class main method:
 * 
 * Step 1:
 * AdjListInputFile name is passed through the command line as args[0]
 * Read from AdjListInputFile with the format:
 * 1. a (int): number of courses in the graph
 * 2. a lines, each with 1 course ID
 * 3. b (int): number of edges in the graph
 * 4. b lines, each with a source ID
 * 
 * Step 2:
 * ValidPreReqInputFile name is passed through the command line as args[1]
 * Read from ValidPreReqInputFile with the format:
 * 1. 1 line containing the proposed advanced course
 * 2. 1 line containing the proposed prereq to the advanced course
 * 
 * Step 3:
 * ValidPreReqOutputFile name is passed through the command line as args[2]
 * Output to ValidPreReqOutputFile with the format:
 * 1. 1 line, containing either the word "YES" or "NO"
 */
public class ValidPrereq {
    public static void main(String[] args) {

        if (args.length < 3) {
            StdOut.println(
                    "Execute: java ValidPrereq <adjacency list INput file> <valid prereq INput file> <valid prereq OUTput file>");
            return;
        }
        // WRITE YOUR CODE HERE
        StdIn.setFile(args[0]);
        ArrayList<classes> allCourses = AdjList.createCourseList();
        StdIn.setFile(args[1]);
        StdOut.setFile(args[2]);
        //StdOut.print(functionname(allCourses));
        boolean result = validToTake("adjlist.in", "validprereq.in", allCourses);
        if(result == false) {
            StdOut.println("NO");
        } else {
            StdOut.println("YES");
        }

    }

    // recursive depth first search
    public static boolean checkingCycle(String course, int prereq, boolean[] checked, ArrayList<classes> allCourses) {
        //ArrayList<classes> allCourses = AdjList.createCourseList();

        if (checked[prereq])
            return false; // cycle exists

        checked[prereq] = true;
        for (int i = 0; i < allCourses.size(); i++) {
            // find the course they want to take
            if (i == prereq) {
                classes num = allCourses.get(i);
                ArrayList<Integer> allPreReqs = num.getIntPreReqs();
                for (int numPreReq : allPreReqs) {
                    boolean result = checkingCycle(course, numPreReq, checked, allCourses);
                    if (result == false) {
                        return false;
                    }

                }
            }
        }

        // go forward and find the pre reqs
        // if next one is empty then go to the list again
        // search for the pre req
        // go right until empty
        // recurse on pre req array list of found course
        // return arrayList<classes> of pre reqs met
        
        return true;
    }

    // method for the YES NO prereq
    public static boolean validToTake(String course1, String course2, ArrayList<classes> allCourses) {
       //ArrayList<classes> allCourses = AdjList.createCourseList();
       boolean[] result = new boolean[allCourses.size()];
        for(int i = 0; i < allCourses.size(); i++) {


            if(allCourses.get(i).getName().equals(course1)) {
                if(!checkingCycle(course1, i, result, allCourses)) {
                    return false;
                }
            }

            if(allCourses.get(i).getName().equals(course2)) {
                if(!checkingCycle(course2, i, result, allCourses)) {
                    return false;
                }
            }

        }
        return true;
    }


}
