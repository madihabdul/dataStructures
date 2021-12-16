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
 * AdjListOutputFile name is passed through the command line as args[1]
 * Output to AdjListOutputFile with the format:
 * 1. c lines, each starting with a different course ID, then 
 *    listing all of that course's prerequisites (space separated)
 */
public class AdjList {
    public static void main(String[] args) {

        if ( args.length < 2 ) {
            StdOut.println("Execute: java AdjList <adjacency list INput file> <adjacency list OUTput file>");
            return;
        }
	// WRITE YOUR CODE HERE

    StdIn.setFile(args[0]);
    StdOut.setFile(args[1]);
    // int point;
    // list = new LinkedList[point]; // create new linked list/array list 

    ArrayList<classes> courses = createCourseList();

    for(int n = 0; n < courses.size(); n++) {
        StdOut.print(courses.get(n).getName() + " "); //getting the acutal title of the course
        for(int m = 0; m < courses.get(n).getPreReqs().size(); m++) {
            StdOut.print(courses.get(n).getPreReqs().get(m) + " "); 
        }

        StdOut.println(); // prints the space
    }

    }

    public static ArrayList<classes> createCourseList() {
        //int num = StdIn.readInt();
        //String courseTitle = StdIn.readLine();
        //System.out.println(courseTitle);
        int courseNum = StdIn.readInt();

        //Integer.ParseInt(num).toString;
        //System.out.println("I'm reading something!");
        String courseTitle = StdIn.readLine();
        ArrayList<classes> courseList = new ArrayList<>(); // courseList read through to see the pre reqs
        for(int i = 0; i < courseNum; i++) { //tried to use for each loop says its non iterable UGGHH
            courseTitle = StdIn.readLine(); //course title
            classes courseID = new classes(); //course title node
            
            courseID.Classes(courseTitle);
            courseList.add(courseID);
        }
        courseNum = StdIn.readInt();
        StdIn.readLine();

         //int requirements = Integer.parseInt(StdIn.readLine());
         while(StdIn.isEmpty() != true) {
            String course = StdIn.readString();
            String req = StdIn.readString();

            for(int i = 0; i < courseList.size(); i++) {
                if(courseList.get(i).getName().equals(course)) {
                    courseList.get(i).takePrePreq(req);
                    break;
                }
            }
         }
        return courseList;
    }


}
