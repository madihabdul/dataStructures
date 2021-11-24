package searchengine;

import java.util.ArrayList;

/*
 *
 * This class represents the occurrences of a word in every movie description it appears.
 * 
 * @author Haolin (Daniel) Jin
 */ 

public class WordOccurrence {

	private String word;                   // the word
	private ArrayList<Location> locations; // all locations where the word occurs
	public WordOccurrence next;

	public WordOccurrence ( String word ) {
		this.word = word;
		locations = new ArrayList<Location>();
		next = null;
	}

	/*
	 * Returns the word
	 * @return word
	 */
	public String getWord (){
		return word;
	}

	/*
	 * Returns all location where word occurs.
	 * @return array containing word's locations.
	 */
	public ArrayList<Location> getLocations (){
		return locations;
	}

	/*
	 * Inserts a new occurrence of @word.
	 * @title the movie's title where @word is located.
	 * @position word's position in the movie's description.
	 */ 
	public void addOccurrence(String title, int position){
		locations.add(new Location(title, position));
	}
	
	/*
	 * Inserts a new ocurrence of @word
	 * @location where @word occurs
	 */
	public void addOccurrence(Location location){
		locations.add(location);
	}

	/*
	 * Returns true if @this equals @other
	 * @other another WordOccurrence object.
	 * @return true if @this equals @other
	 */ 
	public boolean equals ( Object other ) {

		if ( !(other instanceof WordOccurrence) )
			return false;

		WordOccurrence o = (WordOccurrence) other;

		if ( !word.equals(o.getWord()) ) 
			return false;

		for ( int i = 0; i < locations.size(); i++ ) {
			if ( !locations.get(i).equals(o.getLocations().get(i)) ) {
				return false;
			}
		}
		return true;
	}

	/*
	 * Returns the string representation of the WordOccurrence object.
	 * @return the string representing this object.
	 */
	public String toString() {

		String ret = "[" + word + ":";

		for ( int i = 0; i < locations.size(); i++ ) {
			Location loc = locations.get(i);
			ret += loc.toString();
			if ( i < locations.size()-1 ) ret += ",";
		}

		ret += "]";
		return ret;
	}
}