package huffman;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collections;

/**
 * This class contains methods which, when used together, perform the entire
 * Huffman Coding encoding and decoding process
 * 
 * @author Ishaan Ivaturi
 * @author Prince Rawal
 */
public class HuffmanCoding {
    /**
     * Writes a given string of 1's and 0's to the given file byte by byte and NOT
     * as characters of 1 and 0 which take up 8 bits each
     * 
     * @param filename  The file to write to (doesn't need to exist yet)
     * @param bitString The string of 1's and 0's to write to the file in bits
     */
    public static void writeBitString(String filename, String bitString) {
        byte[] bytes = new byte[bitString.length() / 8 + 1];
        int bytesIndex = 0, byteIndex = 0, currentByte = 0;

        // Pad the string with initial zeroes and then a one in order to bring
        // its length to a multiple of 8. When reading, the 1 signifies the
        // end of padding.
        int padding = 8 - (bitString.length() % 8);
        String pad = "";
        for (int i = 0; i < padding - 1; i++)
            pad = pad + "0";
        pad = pad + "1";
        bitString = pad + bitString;

        // For every bit, add it to the right spot in the corresponding byte,
        // and store bytes in the array when finished
        for (char c : bitString.toCharArray()) {
            if (c != '1' && c != '0') {
                System.out.println("Invalid characters in bitstring");
                System.exit(1);
            }

            if (c == '1')
                currentByte += 1 << (7 - byteIndex);
            byteIndex++;

            if (byteIndex == 8) {
                bytes[bytesIndex] = (byte) currentByte;
                bytesIndex++;
                currentByte = 0;
                byteIndex = 0;
            }
        }

        // Write the array of bytes to the provided file
        try {
            FileOutputStream out = new FileOutputStream(filename);
            out.write(bytes);
            out.close();
        } catch (Exception e) {
            System.err.println("Error when writing to file!");
        }
    }

    /**
     * Reads a given file byte by byte, and returns a string of 1's and 0's
     * representing the bits in the file
     * 
     * @param filename The encoded file to read from
     * @return String of 1's and 0's representing the bits in the file
     */
    public static String readBitString(String filename) {
        String bitString = "";

        try {
            FileInputStream in = new FileInputStream(filename);
            File file = new File(filename);

            byte bytes[] = new byte[(int) file.length()];
            in.read(bytes);
            in.close();

            // For each byte read, convert it to a binary string of length 8 and add it
            // to the bit string
            for (byte b : bytes) {
                bitString = bitString + String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0');
            }

            // Detect the first 1 signifying the end of padding, then remove the first few
            // characters, including the 1
            for (int i = 0; i < 8; i++) {
                if (bitString.charAt(i) == '1')
                    return bitString.substring(i + 1);
            }

            return bitString.substring(8);
        } catch (Exception e) {
            System.out.println("Error while reading file!");
            return "";
        }
    }

    /**
     * Reads a given text file character by character, and returns an arraylist of
     * CharFreq objects with frequency > 0, sorted by frequency
     * 
     * @param filename The text file to read from
     * @return Arraylist of CharFreq objects, sorted by frequency
     */
    public static ArrayList<CharFreq> makeSortedList(String filename) {
        StdIn.setFile(filename);

        // create new arraylist that outputs the frequency
        ArrayList<CharFreq> list = new ArrayList<>(); // how many times it occurs
        int[] ascii = new int[256];
        double size = 0;

        while (StdIn.hasNextChar()) { // reads through text file char by char
            var val = StdIn.readChar();
            ascii[(int) val]++;
            size++;
        }

        for (int i = 0; i < ascii.length; i++) {
            if (ascii[i] != 0) {
                CharFreq newNode = new CharFreq((char) i, ascii[i] / size);
                list.add(newNode);
            }
        }

        // case where theres only 1
        if(list.size() == 1) {
            char char1 = list.get(0).getCharacter();
            int val = ((int) char1);
            val++;
            char newChar = (char)val;
            CharFreq finalChar = new CharFreq(newChar, 0);
            list.add(finalChar);
        }

        Collections.sort(list);
        return list;

        // counts how many symbols appear each time -- character counter
        // gives freq by # of times symbols appear each time 4*size -- frequency counter
        // sorts by ascending frequency
        // Collections.sort(list);

        // special case where only one character
        // the one character is root?
    }

    /**
     * Uses a given sorted arraylist of CharFreq objects to build a huffman coding tree
     * 
     * @param sortedList The arraylist of CharFreq objects to build the tree from
     * @return A TreeNode representing the root of the huffman coding tree
     */
    public static TreeNode makeTree(ArrayList<CharFreq> sortedList) {
        
        // create two queues, source has all the sortedList stuff, target is empty
        Queue<TreeNode> source = new Queue<TreeNode>();
        Queue<TreeNode> target = new Queue<TreeNode>();

        TreeNode root = new TreeNode();

        // charfreq variable inside for loop
        // for loop goes through array list size, new TreeNode every iteration that contains CharFreq
        // enqueue CharFreq into sourcenode

        // arrayList into queue ^process
        for(int i = 0; i < sortedList.size(); i++) {
            TreeNode newNode = new TreeNode();
            newNode.setData(sortedList.get(i)); 
            source.enqueue(newNode);
        }

        while(source.isEmpty() != true || target.size() > 1) {

            TreeNode first;
            TreeNode second;

            // case: source is empty
            if(source.isEmpty()) {
                first = target.dequeue();
                second = target.dequeue();
            }
            
            // case: target is empty
            else if (target.isEmpty()) {                
                first = source.dequeue();
                second = source.dequeue();

            } else { // source and target are full -- have values
                TreeNode sourceNode = source.peek();
                TreeNode targetNode = target.peek();
                if (sourceNode.getData().getProbOccurrence() <= targetNode.getData().getProbOccurrence()) {
                    first = source.dequeue();

                    if(source.isEmpty()) {
                        second = target.dequeue();
                    } else {
                        sourceNode = source.peek();

                        if(sourceNode.getData().getProbOccurrence() <= targetNode.getData().getProbOccurrence()) {
                            second = source.dequeue();
                        } else {
                            second = target.dequeue();
                        }
                    }
                    
                } else {
                    first = target.dequeue();
                    if(target.isEmpty()) {
                        second = source.dequeue();
                    } else {
                        targetNode = target.peek();

                        if(sourceNode.getData().getProbOccurrence() <= targetNode.getData().getProbOccurrence()) {
                            second = source.dequeue();
                        } else {
                            second = target.dequeue();
                        }
                    }

                }

            }    

            CharFreq parentProb = new CharFreq(null, first.getData().getProbOccurrence() + second.getData().getProbOccurrence());
            TreeNode parent = new TreeNode(parentProb, first, second);
            target.enqueue(parent);   

        }
        // basically, making parent node that adds left and right nodes
        // enqueuing those nodes to parent
        // return root
      
       root = target.dequeue(); 
       
       return root;
    }

    /**
     * Uses a given huffman coding tree to create a string array of size 128, where
     * each index in the array contains that ASCII character's bitstring encoding.
     * Characters not present in the huffman coding tree should have their spots in
     * the array left null
     * 
     * @param root The root of the given huffman coding tree
     * @return Array of strings containing only 1's and 0's representing character
     *         encodings
     */

    private static String traverse(TreeNode root, String currentString, char target) {
        if(root != null) {
            //return currentString;
            if(root.getLeft() == null && root.getRight() == null) {
                if(root.getData().getCharacter() == target) {
                    return currentString;
                } else {
                    return null;
                }
            }
            else {
                //String newLeft = currentString += "0";
                //String newRight = currentString += "1";
                String left, right = null;
                //System.out.println(target);
                String originalString = currentString;
                if(target == 'a') 
                {
                    //System.out.println("Before:" + currentString);
                     left = traverse(root.getLeft(), currentString += "0", target);
                     currentString = originalString;
                    //System.out.println("After:" + currentString);
                     right = traverse(root.getRight(), currentString += "1", target);
                }
                else 
                {
                    left = traverse(root.getLeft(), currentString += "0", target);
                    currentString = originalString;
                    right = traverse(root.getRight(), currentString += "1", target);
                }
                
                if(left != null) {
                    return left;
                } else if(right != null) {
                    return right;
                } else {
                    return null;
                }
            }            
        } 
        else {
            return null;
        }
    }

    public static String[] makeEncodings(TreeNode root) {

        String[] ascii = new String[128];
        //TreeNode head;
        char target;

        // take root and iterate through huffman tree
        // iterate through ascii character list
        // if match between huffman tree iteration and ascii char list iteration, index becomes 1
        // otherwise index = 0;
        // return updated ascii with 1s and 0s

        for(int i = 0; i < ascii.length; i++) {
            String currentString = "";
            target = (char)i;
            ascii[i] = traverse(root, currentString, target);
            //System.out.println("-----------------");
            }

        return ascii;
    }

    /**
     * Using a given string array of encodings, a given text file, and a file name
     * to encode into, this method makes use of the writeBitString method to write
     * the final encoding of 1's and 0's to the encoded file.
     * 
     * @param encodings   The array containing binary string encodings for each
     *                    ASCII character
     * @param textFile    The text file which is to be encoded
     * @param encodedFile The file name into which the text file is to be encoded
     */
    public static void encodeFromArray(String[] encodings, String textFile, String encodedFile) {
        StdIn.setFile(textFile);

        String newString = "";
        while (StdIn.hasNextChar()) {
            // use textFile in order to see which strings exist
            // if value exists for encodings, set string index = 1 otherwise 0
            // set it equal to string encodedFile
            int index = (int) (StdIn.readChar());
            newString = newString + encodings[index];
        }
        writeBitString(encodedFile, newString);
    }

    /**
     * Using a given encoded file name and a huffman coding tree, this method makes
     * use of the readBitString method to convert the file into a bit string, then
     * decodes the bit string using the tree, and writes it to a file.
     * 
     * @param encodedFile The file which contains the encoded text we want to decode
     * @param root        The root of your Huffman Coding tree
     * @param decodedFile The file which you want to decode into
     */
    public static void decode(String encodedFile, TreeNode root, String decodedFile) {
        StdOut.setFile(decodedFile);

        // readBitString for decoded file
        String read = readBitString(encodedFile);
        String newString = "";
        TreeNode pointer = root;

        for(int i = 0; i < read.length(); i++) {
            if(read.charAt(i) == '0') {
                pointer = pointer.getLeft();
            }
             else if (read.charAt(i) == '1') {
                 pointer = pointer.getRight();
             }
             if(pointer.getLeft() == null && pointer.getRight() == null) {
                char leaf = pointer.getData().getCharacter(); 
                newString = newString + leaf;
                pointer = root;
             }
        }
        // go through huffman tree to see matches until leaf node is met
        // set the matched leaf node value into decoded file
        // start again
        decodedFile = newString;
        StdOut.print(decodedFile);

    }
}
