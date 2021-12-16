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
 * NeedToTakeInputFile name is passed through the command line as args[1]
 * Read from NeedToTakeInputFile with the format:
 * 1. One line, containing a course ID
 * 2. c (int): Number of courses
 * 3. c lines, each with one course ID
 * 
 * Step 3:
 * NeedToTakeOutputFile name is passed through the command line as args[2]
 * Output to NeedToTakeOutputFile with the format:
 * 1. Some number of lines, each with one course ID
 */
public class NeedToTake {
    public static void main(String[] args) {

        if (args.length < 3) {
            StdOut.println(
                    "Execute: java NeedToTake <adjacency list INput file> <need to take INput file> <need to take OUTput file>");
            return;
        }

        // WRITE YOUR CODE HERE

        StdIn.setFile(args[0]);
        StdOut.setFile(args[2]);

        ArrayList<classes> finalForm = thisismyFINALFORM();
        StdIn.setFile(args[1]);
        String courseWanted = StdIn.readLine();

        ArrayList<String> requiredClasses = new ArrayList<String>();
        ArrayList<String> tempList = new ArrayList<String>(); //tempList = finalList

        classes targetCourse = null;

        // find course wanted through list and it pre requisites
        for (int m = 0; m < finalForm.size(); m++) {
            if (!finalForm.get(m).getName().equals(courseWanted))
                continue;
            else
                targetCourse = finalForm.get(m);
        }

        // finds classes taken and adds to list if not present
        ArrayList<String> taken = classesTaken(finalForm);

        // gets classes that are left to take with so many parameters GOD
        requiredClasses = needToTake(taken, requiredClasses, targetCourse, finalForm);

        // add classes to list if not present
        for (String course : requiredClasses) {
            if (!tempList.contains(course))
                tempList.add(course);
                
        }

        // final result
        printList(tempList);

    } // END OF MAIN METHOD

    public static void printList(ArrayList<String> printList) {
        for (int i = 0; i < printList.size(); i++) {
            StdOut.println(printList.get(i));
        }
    }

    public static ArrayList<String> needToTake(ArrayList<String> taken, ArrayList<String> needToTake, classes courseWanted, ArrayList<classes> DONE) {
        ArrayList<String> requirementsNeeded = courseWanted.getPreReqs();
        classes classIterate = null;
        if (!taken.contains(courseWanted.getName())) {
            for (int i = 0; i < requirementsNeeded.size(); i++) {
                String HOLDIT = requirementsNeeded.get(i);
                if (taken.contains(HOLDIT)) {
                    continue;
                }
                for(int m = 0; m < DONE.size(); m++) {
                    if(DONE.get(m).getName().equals(HOLDIT)) {
                        classIterate = DONE.get(m);
                    }
                }
                courseWanted = classIterate;
                needToTake.add(HOLDIT);
                ArrayList<String> neededCourses = needToTake(taken, needToTake, courseWanted, DONE);
                needToTake.addAll(neededCourses);
            }
        } else {

            return needToTake;

        }
        return needToTake;
    }

    // get list of classes taken -- help
    public static ArrayList<String> classesTaken(ArrayList<classes> adjList) {
        int courseSize = Integer.parseInt(StdIn.readLine());
        ArrayList<String> done = new ArrayList<String>();
        ArrayList<String> FORM = new ArrayList<String>();

        for (int i = 0; i < courseSize; i++) {
            String HOLDIT = StdIn.readLine();
            done.add(HOLDIT);
        }

        for (int i = 0; i < done.size(); i++) {
            String name = done.get(i);
            classes currentCourse = null;

            for (int n = 0; n < adjList.size(); n++) {
                if (!adjList.get(n).getName().equals(name)) {
                } else {
                    currentCourse = adjList.get(n);
                }
            }

            ArrayList<String> needed = new ArrayList<String>();
            needed = classesTaken(adjList, currentCourse, needed);

            FORM.add(name);
            FORM.addAll(needed);

        }

        int zero = 0; // weird thing i did for testing size, changed to compile

        //ArrayList<String> coursesDone = new ArrayList<String>();
        ArrayList<String> coursesDone = new ArrayList<String>();
        if(zero != 0) {
        FORM = traverseAdd(coursesDone);

        }
        for (String element : coursesDone) {
            if (!FORM.contains(element)) {
                FORM.add(element);
            }
        }

        // // return the new list
        return FORM;

    }

    // recursively find all courses that were taken -- help
    public static ArrayList<String> classesTaken(ArrayList<classes> finalForm, classes currentCourse, ArrayList<String> needed) {
        
        String HOLDIT; // temp course that needs to be added to the final list
        classes courseWanted; // class that is requested

        if (!currentCourse.getPreReqs().isEmpty()) { 
            needed.addAll(currentCourse.getPreReqs());
            ArrayList<String> neededList = currentCourse.getPreReqs();
            for (int i = 0; i < neededList.size(); i++) {

                HOLDIT = neededList.get(i);
                courseWanted = null;

                for (int m= 0; m < finalForm.size(); m++) {
                    if (finalForm.get(m).getName().equals(HOLDIT)) {
                        courseWanted = finalForm.get(m);
                    }
                }
                ArrayList<String> finalList = classesTaken(finalForm, courseWanted, needed);
                needed.addAll(finalList);
            }

        } else if(currentCourse.getPreReqs().isEmpty()){
            return needed;
        }

        return needed;
    }

    // method that formats list
    public static ArrayList<classes> thisismyFINALFORM() {
        int courseNum = StdIn.readInt();

        String courseTitle = StdIn.readLine();
        ArrayList<classes> courseList = new ArrayList<classes>(); // courseList read through to see the pre reqs
        for (int i = 0; i < courseNum; i++) { // tried to use for each loop says its non iterable UGGHH
            courseTitle = StdIn.readLine(); // course title
            classes courseID = new classes(); // course title node

            courseID.Classes(courseTitle);
            courseList.add(courseID);
        }
        courseNum = StdIn.readInt();
        StdIn.readLine();

        while (StdIn.isEmpty() != true) {
            String course = StdIn.readString();
            String needed = StdIn.readString();

            for (int i = 0; i < courseList.size(); i++) {
                if (courseList.get(i).getName().equals(course)) {
                    courseList.get(i).takePrePreq(needed);
                    break;
                }
            }
        }
        return courseList;
    }

    // METHODS GRAVEYARD -- easier to just write once in main

    // method that adds elements needed for specific list
    public static ArrayList<String> traverseAdd(ArrayList<String> addedList) {

        ArrayList<String> newList = new ArrayList<String>();
        ArrayList<String> prevList = new ArrayList<String>();

        // Traverse through the first list
        for (String course : prevList) {
            if (!newList.contains(course))
                newList.add(course);
        }
        return newList;
    }

    public static classes getCourseWanted(ArrayList<classes> done) {

        String readCourse = StdIn.readLine();
        classes courseWanted = null;

        for (int i = 0; i < done.size(); i++) {
            if (done.get(i).getName().equals(readCourse))
                courseWanted = done.get(i);
        }

        return courseWanted;

    }

}

