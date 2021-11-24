package searchengine;

import java.util.ArrayList;

/*
 * This class represents the distance between two searched words from the same movie description.
 * The searched words are referred as wordA and wordB.
 * 
 */
public class MovieSearchResult implements Comparable<MovieSearchResult> {

    private String title;                      // title of the movie
    private int    minDistance;                // the minimum distance between two locations of wordA and wordB
    private ArrayList<Integer> wordALocations; // holds wordA's locations in the movie's description.
    private ArrayList<Integer> wordBLocations; // holds wordB's locations int the movie's description.

    public MovieSearchResult (String title) {
        this.title       = title;
        this.minDistance = -1;
        this.wordALocations  = new ArrayList<Integer>();
        this.wordBLocations  = new ArrayList<Integer>();
    }
    
    /*
     * @return movie title
     */ 
    public String getTitle(){
        return this.title;
    }

    /*
     * Updates the title
     * @title the movie's title that this object refers to
     */ 
    public void setTitle(String title){
        this.title = title;
    }

    /*
     * @return the shortest distance between the words, wordA and wordB
     */ 
    public int getMinDistance(){
        return this.minDistance;
    }

    /*
     * Updates the minimum distance betwee the words wordA and wordB
     */ 
    public void setMinDistance(int minDistance){
        this.minDistance = minDistance;
    }

    /*
     * @return the locations array for wordA 
     */ 
    public ArrayList<Integer> getArrayListA(){
        return this.wordALocations;
    }

    /*
     * @return the locations array for wordB
     */ 
    public ArrayList<Integer> getArrayListB(){
        return this.wordBLocations;
    }

    /*
     * Adds location to the end of arrayListA
     */
    public void addOccurrenceA(int a){
        this.wordALocations.add(a);
    }

    /*
     * Adds location to the end of arrayListB
     */
    public void addOccurrenceB(int b){
        this.wordBLocations.add(b);
    }
    
    /*
     * compareTo for Collections.sort() to work in topTenSearch
     * 
     * @return the value 0 is the argument @other equals this.  A
     * value less than 0 is this.getMinDistance() is less than 
     * other.getMinDistance(). A value greater than 0 is this.getMinDistance()
     * is greater than other.getMinDistance()
     */ 
	public int compareTo (MovieSearchResult other){
        int  selfMin = minDistance;
        int otherMin = other.getMinDistance();
        if ( selfMin == -1 )   selfMin = Integer.MAX_VALUE;
        if ( otherMin == -1 ) otherMin = Integer.MAX_VALUE;
        
		return selfMin - otherMin;
	}
}
