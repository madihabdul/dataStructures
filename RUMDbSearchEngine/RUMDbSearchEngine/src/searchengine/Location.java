package searchengine;

/*
 * This class represents a word location on a movie's description.
 * 
 * Movies have a title and a description, the word is located in the description.
 * The position refers to the word's position in the description. 
 * The first word is at position 1, second word is at postion 2 and so on.
 * 
 * In the following movie title and description:
 * 
 * Miss Jerry| The adventures of a female reporter in the 1890s.;
 * 
 * Miss Jerry is the movie's title. "The" the word at position 1, "adventures" 
 * the word at position 2, and so on.
 * 
 * @author Haolin (Daniel) Jin
 */
public class Location {

    private String title; // movie's title
    private int position; // position of the word in the movie's description

    public Location (String title, int position){
        this.title = title;
        this.position = position;
    }

    /*
     * @return movie's title
     */
    public String getTitle(){
        return title;
    }

    /*
     * @return position of the word in the movie's description
     */
    public int  getPosition(){
        return position;
    }

    /*
     * Updates movie title
     * 
     * @title 
     */ 
    public void setTitle(String title){
        this.title = title;
    }

    /*
     * Updates the postion of the word in the movie's description
     * @position the order in which the word appears in the description
     */
    public void setPosition(int position){
        this.position = position;
    }

    /*
     * @return the string represenation of Location
     */ 
    public String toString() {
        return title + "(" + position + ")";
    }

    /*
	 * Returns true if @this equals @other
	 * @other another Location object.
	 * @return true if @this equals @other
	 */ 
	public boolean equals ( Object other ) {

		if ( !(other instanceof Location) )
			return false;
        
        Location o = (Location)other;
            
        return title.equals(o.getTitle()) && position == o.getPosition();
    }

}