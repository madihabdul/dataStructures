import java.util.ArrayList;

/**
 * This class contains methods which perform various operations on a layered
 * linked list to simulate transit
 * 
 * @author Ishaan Ivaturi
 * @author Prince Rawal
 */
public class Transit {
	/**
	 * Makes a layered linked list representing the given arrays of train stations,
	 * bus stops, and walking locations. Each layer begins with a location of 0,
	 * even though the arrays don't contain the value 0.
	 * 
	 * @param trainStations Int array listing all the train stations
	 * @param busStops      Int array listing all the bus stops
	 * @param locations     Int array listing all the walking locations (always
	 *                      increments by 1)
	 * @return The zero node in the train layer of the final layered linked list
	 */
	public static TNode makeList(int[] trainStations, int[] busStops, int[] locations) {

		TNode head = null; // the entire thing

		// pointers that start at 0
		TNode walk_ptr = new TNode();
		TNode bus_ptr = new TNode();
		TNode train_ptr = new TNode();

		// set up for pointing to set to 0
		TNode walk_head = walk_ptr;
		TNode bus_head = bus_ptr;

		// points trains 0 (upper left corner) to 0's of other layers
		train_ptr.down = bus_head;
		bus_head.down = walk_head;

		head = train_ptr; // for end

		// walking layer
		for (int i = 0; i < locations.length; i++) {
			TNode temp = new TNode(locations[i]); // adds in locations
			walk_ptr.next = temp; // goes to the next value w temp
			walk_ptr = walk_ptr.next;

		}

		walk_ptr = walk_head; // resets walk location to 0

		// bus layer
		for (int i = 0; i < busStops.length; i++) {
			TNode temp = new TNode(busStops[i]);
			bus_ptr.next = temp;
			bus_ptr = bus_ptr.next;

			while (walk_ptr.location != bus_ptr.location) {
				walk_ptr = walk_ptr.next;
			}

			bus_ptr.down = walk_ptr;
		}

		bus_ptr = bus_head; // resets bus location to 0

		// train layer
		for (int i = 0; i < trainStations.length; i++) {
			TNode temp = new TNode(trainStations[i]);
			train_ptr.next = temp;
			train_ptr = train_ptr.next;

			while (bus_ptr.location != train_ptr.location) {
				bus_ptr = bus_ptr.next;
			}

			train_ptr.down = bus_ptr;
		}
		return head; // returns zero node in train layer
	}

	/**
	 * Modifies the given layered list to remove the given train station but NOT its
	 * associated bus stop or walking location. Do nothing if the train station
	 * doesn't exist
	 * 
	 * @param trainZero The zero node in the train layer of the given layered list
	 * @param station   The location of the train station to remove
	 */
	public static void removeTrainStation(TNode trainZero, int station) {

		// trainZero = head

		TNode head = trainZero;
		TNode prev = trainZero;

		while (head != null) {
			if (head.location == station) {
				prev.next = head.next;
				break;
			}
			prev = head;
			head = head.next;
		}

	}

	/**
	 * Modifies the given layered list to add a new bus stop at the specified
	 * location. Do nothing if there is no corresponding walking location.
	 * 
	 * @param trainZero The zero node in the train layer of the given layered list
	 * @param busStop   The location of the bus stop to add
	 */
	public static void addBusStop(TNode trainZero, int busStop) {
		// WRITE YOUR CODE HERE

		TNode busHead = trainZero.down;
		TNode current = busHead;
		TNode prev = busHead;

		TNode busPtr = busHead;

		TNode temp_pointer = busHead;
		TNode newStop = new TNode(busStop);
		TNode walk_match = trainZero.down.down;

		while (temp_pointer.next != null) {
			temp_pointer = temp_pointer.next; // end of list value
		}
		// case 1: in between nodes
		if (temp_pointer.location > busStop) {

			while (busPtr.next != null) {
				// right and left
				if (busPtr.location < busStop && busPtr.next.location > busStop) { // this is the right of the value to
																					// be added
					// update pointers of new node and old node
					newStop.next = busPtr.next;
					busPtr.next = newStop;
				}
				busPtr = busPtr.next;
			}

			while (walk_match != null) {

				if (walk_match.location == busStop) {
					newStop.down = temp_pointer; // points new node to old's nodes next
					newStop.down = walk_match; // connects new bys stop to walking layer
					break;
				}
				walk_match = walk_match.next;
			}
		}
		// case 2: add bus stop at the end
		if (temp_pointer.location < busStop) {
			// points to new end bus stop value(?)
			temp_pointer.next = newStop;

			System.out.println("Walking Match: " + walk_match.location);
			// checks for walking match connection
			while (walk_match.location != busStop) {
				walk_match = walk_match.next;
				if (walk_match.location == busStop) {
					newStop.down = walk_match;
				}
			}
		}

		// case 3: bus stop already exists
		if (temp_pointer.location == busStop) {
			return;
		}

		// connect bus stops to existing walking path
	}

	/**
	 * Determines the optimal path to get to a given destination in the walking
	 * layer, and collects all the nodes which are visited in this path into an
	 * arraylist.
	 * 
	 * @param trainZero   The zero node in the train layer of the given layered list
	 * @param destination An int representing the destination
	 * @return
	 */

	public static ArrayList<TNode> bestPath(TNode trainZero, int destination) {

		ArrayList<TNode> path = new ArrayList<TNode>();

		TNode busList = trainZero.down;
		TNode bus_ptr = busList;

		TNode walkList = trainZero.down.down;
		TNode walk_ptr = walkList;

		TNode pointer = trainZero; // pointer starts at train Zero
		TNode prev = pointer;

		while (pointer != null) {
			// path.add(pointer);
			// prev = pointer; // trails behind pointer
			// pointer = pointer.next; // checks next item to compare what to do next

			// if(pointer.next == null) {
			// //System.out.println("Pointer Location" + pointer.location);

			// path.add(pointer);

			// if(pointer.location < destination) {
			// pointer = pointer.down;
			// } else {
			// pointer = pointer.next;
			// }
			// }

			System.out.println("no if" + pointer.location);

			if (pointer.location > destination) {
				pointer = prev.down;
				System.out.println("previous " + pointer);

			} else if (pointer.location == destination) {
				path.add(pointer);
				prev = pointer;
				pointer = pointer.down;

				while (pointer != null) {
					path.add(pointer);
					pointer = pointer.down; // Problem: goes down only once instead of twice
				}
				// System.out.println("in if" + pointer.location);
				break;

			} else if (pointer.location < destination) {
				path.add(pointer);
				prev = pointer;
				pointer = pointer.next;
			}
			System.out.println("Previous: " + prev.location);
			if (pointer == null) {
				pointer = prev.down;
				path.add(pointer);
				System.out.println("Pointer: " + pointer.location);
			}
			// if (pointer.location == prev.location) {
			// prev = pointer;
			// pointer = pointer.next;
			//
			if (prev.down == pointer) {
				path.add(pointer);
				prev = pointer;
				pointer = pointer.next;

			}

		}
		// System.out.println("Previous: " + prev.location);
		// if (pointer == null) {
		// pointer = prev.down;
		// System.out.println("Pointer: " + pointer.location);
		// }
		// // if (pointer.location == prev.location) {
		// // prev = pointer;
		// // pointer = pointer.next;
		// //
		// if (prev.down == pointer) {
		// prev = pointer;
		// pointer = pointer.next;
		// }
		return path;
	}

	/**
	 * Returns a deep copy of the given layered list, which contains exactly the
	 * same locations and connections, but every node is a NEW node.
	 * 
	 * @param trainZero The zero node in the train layer of the given layered list
	 * @return
	 */
	public static TNode duplicate(TNode trainZero) {

		// across, get the nodes, go down when the layer is over
		// when pointer = null
		// then move pointer
		// something that can tell if theres a connection with the layer below it
		// needs to check if the list is over with the last layer (down.down == null &&
		// pointer.next == null?)

		// TNode head = trainZero;
		// TNode pointer = trainZero;
		// TNode downLayer = trainZero;

		// TNode list_copy = new TNode();

		// ArrayList<Integer> newList = new ArrayList<Integer>();
		// ArrayList<int[]> listOfLists = new ArrayList<int[]>();

		// while (pointer != null) { // pointer rn is going through the first layer of
		// the train layer
		// newList.add(pointer.location);
		// pointer = pointer.next;
		// if (pointer == null) {
		// int[] layer = newList.toArray();
		// pointer = downLayer.down;
		// downLayer = pointer;

		// }
		// }
		TNode head = trainZero; // the entire thing

		TNode trainLayer = trainZero; // train head
		TNode t_pointer = trainLayer;

		TNode busLayer = trainZero.down; // bus head

		TNode walkLayer = trainZero.down.down; // walk head

		TNode prev = null;
		TNode newTrainHead = null;
		TNode newBusHead = null;
		TNode newWalkHead = null;

		// walk layer
		while (walkLayer != null) {
			TNode walkCopy = new TNode();
			if (newWalkHead == null) {
				newWalkHead = walkCopy;
			}
			walkCopy.location = walkLayer.location;
			if (prev != null) {
				prev.next = walkCopy;
			}
			prev = walkCopy;
			walkLayer = walkLayer.next;
		}

		// bus layer
		prev = null;
		while (busLayer != null) {
			TNode busCopy = new TNode();
			if (newBusHead == null) {
				newBusHead = busCopy;
			}
			busCopy.location = busLayer.location;

			TNode curr = newWalkHead;
			while(curr.location != busCopy.location) {
				curr = curr.next;
			}

			busCopy.down = curr;

			if (prev != null) {
				prev.next = busCopy;
			}
			prev = busCopy;
			busLayer = busLayer.next;
		}
		// train Layer
		prev = null;
		while (trainLayer != null) {
			TNode trainCopy = new TNode();
			if (newTrainHead == null) {
				newTrainHead = trainCopy;
			}
			trainCopy.location = trainLayer.location;

			TNode curr = newBusHead;
			while(curr.location != trainCopy.location) {
				curr = curr.next;

			}

			trainCopy.down = curr;

			if (prev != null) {
				prev.next = trainCopy;
			}
			prev = trainCopy;
			trainLayer = trainLayer.next;
		}

		return newTrainHead;
	}

	/**
	 * Modifies the given layered list to add a scooter layer in between the bus and
	 * walking layer.
	 * 
	 * @param trainZero    The zero node in the train layer of the given layered
	 *                     list
	 * @param scooterStops An int array representing where the scooter stops are
	 *                     located
	 */
	public static void addScooter(TNode trainZero, int[] scooterStops) {

		// Problems: extra zero at scooter.txt file, the scooter layer does not go into
		// the linked list
		// however the program is able to populate the values, just need to get it into
		// the scooter layer and
		// connect the nodes

		TNode pointer = trainZero;
		ArrayList<TNode> scooter_layer = new ArrayList<TNode>();

		TNode bus_head = trainZero.down;
		TNode bus_ptr = bus_head;

		TNode walk_head = trainZero.down.down;
		TNode walk_ptr = walk_head;

		TNode scooterHead = new TNode(); // beginning of scooter linked list
		TNode scooterPointer = new TNode();
		scooterHead = scooterPointer;
		TNode scooter_prev = new TNode();

		// create nodes for linked list and values
		for (int i = 0; i < scooterStops.length; i++) {

			TNode temp = new TNode(scooterStops[i]);
			// System.out.println(temp);
			scooterPointer.next = temp; // connects values to each other to form linked list
			scooterPointer = scooterPointer.next;
		}

		// System.out.println("-------");
		// all scooter stops need to correspond to bus stops, creating bus stops
		// every bus stop needs a scooter link, not every scooter needs to be near bus
		// stop

		scooterPointer = scooterHead;
		scooter_prev = scooterHead;
		// while (bus_ptr != null) {

		// while(scooterPointer != null) {

		// //case 2: the location will be at the end of the list => ie: put the node at
		// the end
		// if (scooterPointer.next == null) {
		// TNode newScooterStop = new TNode(); // connects to bus stops
		// scooterPointer.next = newScooterStop;
		// scooterPointer = scooterPointer.next;
		// System.out.println("Case 2");

		// }

		// //case 3: putting it between two different nodes 1 and 3 => 1 2 3
		// if(bus_ptr.location > scooter_prev.location && bus_ptr.location <
		// scooterPointer.location) {

		// TNode newBusStop = new TNode();
		// scooter_prev.next = newBusStop; //scooter previous is before the new bus sto
		// newBusStop.next = scooterPointer; //scooterPointer is after the new bus stop
		// System.out.println("Case 3");

		// }
		// scooter_prev = scooterPointer;
		// scooterPointer = scooterPointer.next;
		// }
		// bus_ptr = bus_ptr.next;
		// }

		// System.out.println("Hello!!!");
		scooterPointer = scooterHead;
		while (scooterPointer != null) {
			// System.out.println(scooterPointer.location);
			scooterPointer = scooterPointer.next;
		}

		// connect scooter list to walking list
		scooterPointer = scooterHead;
		// walk_ptr = walk_head; -- already connected at the top
		while (scooterPointer != null) {
			if (scooterPointer.location == walk_ptr.location) {
				scooterPointer.down = walk_ptr; // scooter position points down to walk
				scooterPointer = scooterPointer.next;
			}
			walk_ptr = walk_ptr.next;
		}

		scooterPointer = scooterHead;

		// connect bus stops to scooter list
		while (bus_ptr != null) {
			if (bus_ptr.location == scooterPointer.location) {
				bus_ptr.down = scooterPointer; // scooter position points down to walk
				bus_ptr = bus_ptr.next;
			}
			scooterPointer = scooterPointer.next;
		}
		// while(bus_ptr != null) {
		// while (scooterPointer!= null) {
		// if (scooterPointer.location == bus_ptr.location) {
		// bus_ptr.down = scooterPointer.down;
		// }
		// scooterPointer = scooterPointer.next;
		// }
		// bus_ptr = bus_ptr.next;
		// }
	}
}