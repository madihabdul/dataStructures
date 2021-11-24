package searchengine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;

/*
 * This class builds a hash table of words from movies descriptions. Each word maps to a set
 * of movies in which it occurs.
 * 
 * @author Haolin (Daniel) Jin
 * @author Ana Paula Centeno
 * 
 */
public class RUMDbSearchEngine {

	private int hashSize; // the hash table size
	private double threshold; // load factor threshold. load factor = wordCount/hashSize
	private int wordCount; // the number of unique words in the table
	private WordOccurrence[] hashTable; // the hash table

	private ArrayList<String> noiseWords; // noisewords are not to be inserted in the hash table

	/*
	 * Constructor initilizes the hash table.
	 * 
	 * @param hashSize is the size for the hash table
	 * 
	 * @param threshold for the hash table load factor. Rehash occurs when the ratio
	 * wordCount : hashSize exceeds the threshold.
	 * 
	 * @param noiseWordsFile contains words that will not be inserted into the hash
	 * table.
	 */
	public RUMDbSearchEngine(int hashSize, double threshold, String noiseWordsFile) {

		this.hashSize = hashSize;
		this.hashTable = new WordOccurrence[hashSize];
		this.noiseWords = new ArrayList<String>();
		this.threshold = threshold;
		this.wordCount = 0;

		// Read noise words from file
		StdIn.setFile(noiseWordsFile);
		while (!StdIn.isEmpty()) {
			String word = StdIn.readString();
			if (!noiseWords.contains(word))
				noiseWords.add(word);
		}
	}

	/*
	 * Method used to map a word into an array index.
	 * 
	 * @param word the word
	 * 
	 * @return array index within @hashTable
	 */
	private int hashFunction(String word) {
		int hashCode = Math.abs(word.toLowerCase().replaceAll("/[^a-z0-9]/", "").hashCode());
		return hashCode % hashSize;
	}

	/*
	 * Returns the hash table load factor
	 * 
	 * @return the load factor
	 */
	public double getLoadFactor() {
		return (double) wordCount / hashSize;
	}

	/*
	 * This method reads movies title and description from the input file.
	 * 
	 * @param inputFile the file to be read containg movie's titles and
	 * descriptions.
	 * 
	 * The inputFile format: Each line describes a movie's title, and a short
	 * description on the movie. title| word1 word2 word3;
	 * 
	 * Note that title can have multiple words, there is no space between the last
	 * word on the tile and '|' No duplicate movie name accepted.
	 * 
	 * @return ArrayList of ArrayList of Strings, each inner ArrayList refers to a
	 * movie, the first index contains the title, the remaining indices contain the
	 * movie's description words (one word per index).
	 * 
	 * Example: [ [full title1][word1][word2] [full title2][word1] [full
	 * title3][word1][word2][word3][word4] ]
	 */
	public ArrayList<ArrayList<String>> readInputFile(String inputFile) {

		ArrayList<ArrayList<String>> allMovies = new ArrayList<ArrayList<String>>();
		StdIn.setFile(inputFile);

		String[] read = StdIn.readAllStrings();

		for (int i = 0; i < read.length; i++) {
			ArrayList<String> movie = new ArrayList<String>();
			String t = "";
			do {
				t += " " + read[i];
			} while (read[i++].indexOf('|') == -1);

			movie.add(t.substring(1, t.length() - 1).toLowerCase().replaceAll("/[^a-z0-9]/", ""));

			while (i < read.length) {
				if (read[i].indexOf(';') != -1) {
					movie.add(read[i].substring(0, read[i].indexOf(';')));
					break;
				}
				movie.add(read[i].toLowerCase().replaceAll("/[^a-z0-9]/", ""));
				i++;
			}
			allMovies.add(movie);
		}
		return allMovies;
	}

	/*
	 * This method calls readInputFile and uses its output to load the movies and
	 * their descriptions words into the hashTable.
	 * 
	 * Use the result from readInputFile() to insert each word and its location into
	 * the hash table.
	 * 
	 * Use isWord() to discard noise words, remove trailing punctuation, and to
	 * transform the word into all lowercase character.
	 * 
	 * Use insertWordLocation() to insert each word into the hash table.
	 * 
	 * Use insertWordLocation() to insert the word into the hash table.
	 * 
	 * @param inputFile the file to be read containg movie's titles and descriptions
	 * 
	 */
	public void insertMoviesIntoHashTable(String inputFile) {

		// hash table that will output movies :)
		// int = key; string = value
		//Hashtable<Integer, String> movieHash = new Hashtable<Integer, String>();
		//StdIn.setFile(inputFile);

		ArrayList<ArrayList<String>> filmList = readInputFile(inputFile);

		// while(filmList != null) {
		for (int n = 0; n < filmList.size(); n++) {
			for (int m = 1; m < filmList.get(n).size(); m++) {
				String movieTitle = filmList.get(n).get(m);
				// cleans up movie title noise words
				String cleanTitle = isWord(movieTitle);
				String firstTitle = filmList.get(n).get(0);
				// WordOccurrence head = getWordOccurrence(movieTitle);
				Location coordinate = new Location(firstTitle, m);
				if (cleanTitle != null) {
					insertWordLocation(cleanTitle, coordinate);
				}
			}
		}
		//print();
	}
	// }

	/**
	 * Given a word, returns it as a word if it is any word that, after being
	 * stripped of any trailing punctuation, consists only of alphabetic letters and
	 * digits, and is not a noise word. All words are treated in a case-INsensitive
	 * manner.
	 * 
	 * Punctuation characters are the following: '.', ',', '?', ':', ';' and '!'
	 * 
	 * @param word Candidate word
	 * @return word (word without trailing punctuation, LOWER CASE)
	 */
	private String isWord(String word) {
		int p = 0;
		char ch = word.charAt(word.length() - (p + 1));
		while (ch == '.' || ch == ',' || ch == '?' || ch == ':' || ch == ';' || ch == '!') {
			p++;
			if (p == word.length()) {
				// the entire word is punctuation
				return null;
			}
			int index = word.length() - (p + 1);
			if (index == -1) {
				System.out.flush();
			}
			ch = word.charAt(word.length() - (p + 1));
		}

		word = word.substring(0, word.length() - p);

		// are all characters alphabetic letters?
		for (int i = 0; i < word.length(); i++) {
			if (!Character.isLetterOrDigit(word.charAt(i))) {
				return null;
			}
		}
		word = word.toLowerCase();
		if (noiseWords.contains(word)) {
			return null;
		}
		return word;
	}

	/*
	 * Prints the entire hash table
	 */
	public void print() {

		for (int i = 0; i < hashTable.length; i++) {

			StdOut.printf("[%d]->", i);
			for (WordOccurrence ptr = hashTable[i]; ptr != null; ptr = ptr.next) {

				StdOut.print(ptr.toString());
				if (ptr.next != null) {
					StdOut.print("->");
				}
			}
			StdOut.println();
		}
	}

	/*
	 * This method inserts a Location object @loc into the matching WordOccurrence
	 * object in the hash table. If the word is not present into the hash table, add
	 * a new WordOccurrence object into hash table.
	 * 
	 * @param word to be inserted
	 * 
	 * @param loc the word's position within the description.
	 */
	public void insertWordLocation(String word, Location loc) {

		// gets location Location wordMatch = new Location (word,loc);

		// case 1: creates new array index if word match is not present in hash table
		if (getWordOccurrence(word) == null) {

			// create new word occurrence
			// add new array index to hash table
			WordOccurrence addWord = new WordOccurrence(word);
			addWord.addOccurrence(loc);
			// increases # of unique words in array list
			wordCount++;

			int newWord = hashFunction(word);
			int newList = newWord;

			if (hashTable[newList] != null) {

				addWord.next = hashTable[newList];
				hashTable[newList] = addWord;

			} else if (hashTable[newList] == null) {

				hashTable[newList] = addWord;
			}

			// case 2: rehash if loadfactor > threshold
			if (getLoadFactor() > threshold) {
				hashSize = hashSize * 2;
				rehash(hashSize);
			}
		}

		// case 3: finds matching word occurence index; inserts word occurrence object
		// into front of linked list
		else if ((getWordOccurrence(word).getWord().equals(word) && getWordOccurrence(word) != null)) {
			getWordOccurrence(word).addOccurrence(loc);
		}
	}

	/*
	 * Rehash the hash table to newHashSize. Rehash happens when the load factor is
	 * greater than the @threshold (load factor = wordCount/hashSize).
	 * 
	 * @param newHashSize is the new hash size
	 */
	private void rehash(int newHashSize) {

		// new hash table location
		WordOccurrence[] resizeTable = new WordOccurrence[newHashSize];
		// get load factor
		// double lF = getLoadFactor();
		// int oldHashSize = hashSize;
		hashSize = newHashSize;

		// rehash method condition
		// old table iteration
		for (WordOccurrence front : hashTable) {
			// going through array list nodes
			for (WordOccurrence first = front; first != null; first = first.next) {
				// get new hashcode index value
				int hashVal = hashFunction(first.getWord());
				WordOccurrence random = new WordOccurrence(first.getWord());

				for(int i = 0; i < first.getLocations().size(); i++) {
					random.addOccurrence(first.getLocations().get(i).getTitle(), first.getLocations().get(i).getPosition());
				}

				if (resizeTable[hashVal] != null) {
					WordOccurrence temp = resizeTable[hashVal];
					resizeTable[hashVal] = random;
					random.next = temp;
				} else {
					resizeTable[hashVal] = random;
				}
				// insert resized nodes into new table
				//WordOccurrence oldFront = resizeTable[hashVal];
				//WordOccurrence newFront = first;
				// oldFirst goes to second position
				//newFront.next = oldFront;
				// takes new word to insert through the front with original hash table
				//resizeTable[hashVal] = newFront;
		}
	}
		hashSize = newHashSize;
		hashTable = resizeTable;
		// set old table to new resized table
	}

	/*
	 * Find the WordOccurrence object with the target word in the hash table
	 * 
	 * @param word search target
	 * 
	 * @return @word WordOccurrence object
	 */
	public WordOccurrence getWordOccurrence(String word) {

		// create object of WordOccurrence to count through
		// count how many times the word exists through the hash array list using a for
		// loop
		// output the WordOccurrence amount by position of pointer

		// for loop for going through hash table indexes
		for (WordOccurrence first : hashTable) {
			// going through linked list at indexes
			for (WordOccurrence curr = first; curr != null; curr = curr.next) {
				if (curr.getWord().equals(word)) {
					return curr;
				}
			}
		}
		return null;
	}

	/*
	 * Finds all occurrences of wordA and wordB in the hash table, and add them to
	 * an ArrayList of MovieSearchResult based on titles. (no need to calculate
	 * distance here)
	 * 
	 * @param wordA is the first queried word
	 * 
	 * @param wordB is the second queried word
	 * 
	 * @return ArrayList of MovieSearchResult objects.
	 */
	public ArrayList<MovieSearchResult> createMovieSearchResult(String wordA, String wordB) {

		// create arrayList where movie search results are held
		//ArrayList<MovieSearchResult> movieListOutput = new ArrayList<MovieSearchResult>();

		// the words that need to be found
		WordOccurrence firstWord = getWordOccurrence(wordA);
		WordOccurrence secondWord = getWordOccurrence(wordB);
		System.out.println(firstWord.getLocations().size());

		// condition where one or both of the words arent found in the hash table XD
		if (firstWord == null || secondWord == null) {
			return null;
		}

		// create movie search result index where BOTH words are found uwu
		// if found, put movie into movieSearchResult array :>
		Hashtable<String, MovieSearchResult> movieSearchHash = new Hashtable<String, MovieSearchResult>();
		if (firstWord != null) {
			ArrayList<Location> wordALocation = firstWord.getLocations();
			for(Location location : wordALocation) {
				// checking if movie exists
				if(!movieSearchHash.containsKey(location.getTitle())) {
					movieSearchHash.put(location.getTitle(), new MovieSearchResult(location.getTitle()));
				} // 
				MovieSearchResult movieSearchResult = movieSearchHash.get(location.getTitle());
				movieSearchResult.addOccurrenceA(location.getPosition());
				System.out.println("Array List A: " + movieSearchResult.getArrayListA());
				//System.out.println("Title of Movie in Location A: " + location.getTitle());
			}
		}

		if(secondWord != null) {
			ArrayList<Location> wordBLocation = secondWord.getLocations();
			System.out.println("Size of Word B: " + wordBLocation.size());
			for(Location location : wordBLocation) {
				// checking if movie exists
				if(!movieSearchHash.containsKey(location.getTitle())) {
					movieSearchHash.put(location.getTitle(), new MovieSearchResult(location.getTitle()));
				}
				MovieSearchResult movieSearchResult = movieSearchHash.get(location.getTitle());
				movieSearchResult.addOccurrenceB(location.getPosition());
				System.out.println("Array List B: " + movieSearchResult.getArrayListB());
				//System.out.println("Title of Movie in Location B " + location.getTitle());
			}
		}
		// returns hash table collection with the values for A and B
		movieSearchHash.values();
		// converts collection to arrayList
		ArrayList<MovieSearchResult> finalMovieList = new ArrayList<MovieSearchResult>(movieSearchHash.values());
		// returns array list of movie search results
		return finalMovieList;
	}

	/*
	 * 
	 * Computes the minimum distance between the two wordA and wordB in @msr. In
	 * another words, this method computes how close these two words appear in the
	 * description of the movie (MovieSearchResult refers to one movie).
	 * 
	 * If the movie's description doesn't contain one, or both words set distance to
	 * -1;
	 * 
	 * NOTE: the ArrayLists for A and B will always be in order since the words were
	 * added in order.
	 * 
	 * The shortest distance between two words can be found by keeping track of the
	 * index of previous wordA and wordB, then find the next location of either word
	 * and calculate the distance between the word and the previous location of the
	 * other word.
	 * 
	 * For example: wordA locations: 1 3 5 11 wordB locations: 4 10 12 start
	 * previousA as 1, and previousB as 4, calculate distance as abs(1-4) = 3
	 * because 1<4, update previousA to 3, abs(4-3) = 1 , smallest so far because
	 * 3<4, update previousA to 5, abs(5-4) = 1 because 5>4, update previousB to 10,
	 * abs(5-10) = 5 because 5<10, update previousA to 11, abs(11-10) = 1 End
	 * because all elements from A have been used.
	 * 
	 * @param msr the MovieSearchResult object to be updated with the minimum
	 * distance between its words.
	 */
	public void calculateMinDistance(MovieSearchResult msr) {

		// array lists A and B that hold the two words locations
		ArrayList<Integer> arrA = msr.getArrayListA();
		ArrayList<Integer> arrB = msr.getArrayListB();

		if(arrA.size() == 0 || arrB.size() == 0) {
			msr.setMinDistance(-1);
			//System.out.println(-1);
		} else {

		// keep track on index1 and index2
		int index1 = 0; // wordA
		int index2 = 0; // wordB
		
		int minDistance = Math.abs(arrA.get(0) - arrB.get(0));
		// END when all the elements from one of the indexes have been used
		while(index1 < arrA.size() && index2 < arrB.size()) {
			// A is smaller -- update index1 (A) location
			if(arrA.get(index1) < arrB.get(index2)) {
				int diff_A_smaller = arrB.get(index2) - arrA.get(index1);
				//diff_A_smaller = Math.abs(diff_A_smaller);
				// keeps track of the min distance so far
				if(minDistance > diff_A_smaller) {
					minDistance = diff_A_smaller; 
				}
				index1++;

			} else { // B is smaller -- update index2 (B) location
				int diff_B_smaller = arrA.get(index1) - arrB.get(index2);
				//diff_B_smaller = Math.abs(diff_B_smaller);
				// keeps track of the min distance so far
				if(minDistance > diff_B_smaller) {
					minDistance = diff_B_smaller;
				}
				index2++;
			}
		}
			// output smallest distance through loop calculation
			msr.setMinDistance(minDistance);
	}
}

	/*
	 * This method's purpose is to search the movie database to find movies that
	 * contain two words (wordA and wordB) in their description.
	 * 
	 * @param wordA the first word to search
	 * 
	 * @param wordB the second word to search
	 * 
	 * @return ArrayList of MovieSearchResult, with length <= 10. Each
	 * MovieSearchResult object returned must have a non -1 distance (meaning that
	 * both words appear in the description). The ArrayList is expected to be sorted
	 * from the smallest distance to the greatest.
	 * 
	 * NOTE: feel free to use Collections.sort( arrayListOfMovieSearchResult ); to
	 * sort.
	 */
	public ArrayList<MovieSearchResult> topTenSearch(String wordA, String wordB) {

		// Error 1: data.txt no matching words pop up
		// Most likely an issue with createMovieSearchResult since every other one is 0

		ArrayList<MovieSearchResult> resultSorted = new ArrayList<MovieSearchResult>();
		ArrayList<MovieSearchResult> movieSearch = createMovieSearchResult(wordA, wordB);

		//System.out.println("Movie Search: " + movieSearch.size());
		//System.out.println("-------------------");
		if(movieSearch == null) {
			return null;
		}
		for(int i = 0; i < movieSearch.size(); i++) {
			calculateMinDistance(movieSearch.get(i));
		}

		resultSorted = movieSearch;
		Collections.sort(resultSorted);
		int i = 0;
		while(i < resultSorted.size()) {
			// if(resultSorted == null) {
			// 	return null;
			// }
			if(resultSorted.get(i).getMinDistance() == -1 || i >= 10) {
			//if(i >= 10) {
			resultSorted.remove(i);
			} else {
				i++;
			}
		}
		return resultSorted;
	}
}
