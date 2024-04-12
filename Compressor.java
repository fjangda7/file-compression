import java.util.Scanner;
import java.util.ArrayList;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.PrintWriter;
import java.io.IOException;

class Compressor {
    public static void main(String[] args) {
        Scanner userInput = new Scanner(System.in);
        System.out.println("Enter name of encoded file: ");
        String encodedFileName = userInput.nextLine() + ".txt";
        userInput.close();

        String text = "";
        try {
            Scanner fileInput = new Scanner(new File("input.txt"));
            text = fileInput.useDelimiter("\\A").next();
            fileInput.close();
        } catch (FileNotFoundException exception) {
            System.out.println("ERROR: 'input.txt' not found: " + exception);
        }

        String finalTree;
        ArrayList<Integer> occurrence = new ArrayList<>();
        ArrayList<String> letter = new ArrayList<>();
        ArrayList<String> tree = new ArrayList<>();
        String[] finalValues;

        occurenceNum(text, letter, occurrence);

        finalValues = new String[letter.size()];

        for (int i = 0; i < letter.size(); i++) {
            tree.add("(" + stringBinaryChar(letter.get(i)) + ")" + occurrence.get(i));
        }

        String[] binaryValues = new String[letter.size()];
        for (int i = 0; i < binaryValues.length; i++) {
            binaryValues[i] = "";
        }

        for (int i = 0; i < occurrence.size(); i++) {
            System.out.println(letter.get(i) + " " + occurrence.get(i));
        }

        finalTree = treeBuilder(tree).get(0);
        finalTree = finalTree.substring(0, finalTree.lastIndexOf(")") + 1);
        System.out.println(finalTree);

        binaryAssigner(finalTree, binaryValues, letter);

        for (int i = 0; i < letter.size(); i++) {
            finalValues[i] = letter.get(i) + binaryValues[i];
        }

        bubbleSort(finalValues, occurrence);

        for (int i = 0; i < finalValues.length; i++) {
            System.out.println(finalValues[i]);
        }

        try {
            FileWriter fw = new FileWriter(encodedFileName, true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter output = new PrintWriter(bw);

            saveTree(finalValues, output);
            encode(finalValues, output, text);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void bubbleSort(String[] finalValues, ArrayList<Integer> occurrence) {
        int tempOccurrence;
        String tempValue;

        for (int j = 0; j <= finalValues.length - 1; j++) {
            for (int i = 0; i <= finalValues.length - 2; i++) {
                if (occurrence.get(i) < occurrence.get(i + 1)) {
                    tempOccurrence = occurrence.get(i);
                    tempValue = finalValues[i];

                    occurrence.set(i, occurrence.get(i + 1));
                    finalValues[i] = finalValues[i + 1];
                    finalValues[i + 1] = tempValue;
                    occurrence.set(i + 1, tempOccurrence);
                }
            }
        }
    }

    public static void binaryAssigner(String treeStr, String[] binaryValues, ArrayList<String> letter) {
        if (treeStr.length() > 5) {
            String newStr;
            int bracketValue = 0;
            newStr = treeStr.substring(1, treeStr.length() - 1);
            String package1, package2;

            int counter = -1;
            do {
                counter++;
                if (newStr.substring(counter, counter + 1).equals("(")) {
                    bracketValue++;
                } else if (newStr.substring(counter, counter + 1).equals(")")) {
                    bracketValue--;
                }

            } while (bracketValue > 0);

            package1 = newStr.substring(0, counter + 1);
            package2 = newStr.substring(counter + 1, newStr.length());

            for (int i = 0; i < package1.length() - 2; i++) {
                for (int j = 0; j < letter.size(); j++) {
                    if (stringBinaryChar(letter.get(j)).equals(package1.substring(i, i + 3))) {
                        binaryValues[j] += "0";
                    }
                }
            }

            for (int i = 0; i < package2.length() - 2; i++) {
                for (int j = 0; j < letter.size(); j++) {
                    if (stringBinaryChar(letter.get(j)).equals(package2.substring(i, i + 3))) {
                        binaryValues[j] += "1";
                    }
                }
            }

            binaryAssigner(package1, binaryValues, letter);
            binaryAssigner(package2, binaryValues, letter);
        }
    }

    public static String stringBinaryChar(String str) {
        int intValue = (int) str.charAt(0);
        if (intValue < 10) {
            return String.format("%03d", intValue);
        } else if (intValue < 100) {
            return String.format("%02d", intValue);
        } else {
            return String.valueOf(intValue);
        }
    }

    public static ArrayList<String> treeBuilder(ArrayList<String> tree) {
        int min1 = Integer.MAX_VALUE;
        int min2 = Integer.MAX_VALUE;
        int indexNum1 = 0;
        int indexNum2 = 0;
        String addedComponents;

        if (tree.size() > 1) {
            for (int i = 0; i < tree.size(); i++) {
                if (Integer.parseInt(tree.get(i).substring(tree.get(i).lastIndexOf(")") + 1)) < min1) {
                    min1 = Integer.parseInt(tree.get(i).substring(tree.get(i).lastIndexOf(")") + 1));
                    indexNum1 = i;
                }
            }
            for (int i = 0; i < tree.size(); i++) {
                if (Integer.parseInt(tree.get(i).substring(tree.get(i).lastIndexOf(")") + 1)) < min2 && i != indexNum1) {
                    min2 = Integer.parseInt(tree.get(i).substring(tree.get(i).lastIndexOf(")") + 1));
                    indexNum2 = i;
                }
            }

            addedComponents = addComponents(tree.get(indexNum1), tree.get(indexNum2));

            if (indexNum1 > indexNum2) {
                tree.remove(indexNum1);
                tree.remove(indexNum2);
            } else {
                tree.remove(indexNum2);
                tree.remove(indexNum1);
            }

            tree.add(addedComponents);

            return treeBuilder(tree);
        } else {
            return tree;
        }
    }

    public static String addComponents(String first, String second) {
        return "(" + first.substring(0, first.lastIndexOf(")") + 1) + second.substring(0, second.lastIndexOf(")") + 1) + ")" +
                (Integer.parseInt(first.substring(first.lastIndexOf(")") + 1)) + Integer.parseInt(second.substring(second.lastIndexOf(")") + 1)));
    }

    public static void occurenceNum(String str, ArrayList<String> letter, ArrayList<Integer> occurrence) {
        for (int i = 0; i < str.length(); i++) {
            String newChar = str.substring(i, i + 1);
            boolean alreadySaved = false;
            int index = 0;

            for (int j = 0; j < letter.size(); j++) {
                if (letter.size() > 0) {
                    if (newChar.equals(letter.get(j))) {
                        alreadySaved = true;
                        index = j;
                    }
                }
            }

            if (letter.size() == 0) {
                letter.add(newChar);
                occurrence.add(0);
                alreadySaved = true;
            }

            if (!alreadySaved) {
                letter.add(newChar);
                occurrence.add(1);
            } else {
                occurrence.set(index, occurrence.get(index) + 1);
            }
        }
    }

    public static void encode(String[] tree, PrintWriter output, String text) throws IOException {
        String encodedBinaryTemp = "";
        String encodedBinary = "";

        for (int i = 0; i < text.length(); i++) {
            String originalChar = text.substring(i, i + 1);

            for (int j = tree.length - 1; j >= 0; j--) {
                if (originalChar.equals(tree[j].substring(0, 1))) {
                    encodedBinaryTemp += tree[j].substring(1);
                    break;
                }
            }

            if (encodedBinaryTemp.length() > 1752) {
                printEncoded(encodedBinaryTemp.substring(0, 1752), output);
                encodedBinaryTemp = encodedBinaryTemp.substring(1752);
            }
        }

        encodedBinary += encodedBinaryTemp;
        int emptyBits = 0;

        for (int i = 0; i < 8 - (encodedBinary.length() % 8); i++) {
            encodedBinary += "0";
            emptyBits++;
        }

        printEncoded(encodedBinary, output);
        output.print(emptyBits);
    }

    public static void saveTree(String[] tree, PrintWriter output) throws IOException {
        for (int i = 1; i < 255; i++) {
            boolean charNotPresent = true;

            for (String s : tree) {
                if ((char) i == s.charAt(0)) {
                    output.println(s.substring(1));
                    charNotPresent = false;
                    break;
                }
            }

            if (charNotPresent) {
                output.println("");
            }
        }
    }

    public static void printEncoded(String encodedBinary, PrintWriter output) {
        for (int i = encodedBinary.length() / 8 - 1; i >= 0; i--) {
            char character = '\0';

            for (int j = 1; j <= 8; j++) {
                if (i > 0 && encodedBinary.substring(j - 1, j).equals("1")) {
                    character = (char) (character | (char) (Math.pow(2, 8 - j)));
                }
            }

            encodedBinary = encodedBinary.substring(8);

            output.print(character);
        }
    }
}
