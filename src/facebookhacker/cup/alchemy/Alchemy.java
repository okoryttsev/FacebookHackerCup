package facebookhacker.cup.alchemy;


import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * created by Oleksandr Koryttsev
 * korytcev@gmail.com
 */
public class Alchemy {

    static List<String> colors = new ArrayList<>();

    static List<Integer> lengths = new ArrayList<>();

    static int caseNumbers;

    private static HashMap<Integer, String> output = new HashMap<>();

    private static Logger logger = Logger.getLogger(Alchemy.class.getName());

    private static String merged;

    private static int insertIndex;

    static LinkedList<Character> colorName = new LinkedList<>();


    public static void main(String[] args) {
//        readDataFromFIle("src/facebookhacker/cup/alchemy_validation_input.txt");
        readDataFromFIle("src/facebookhacker/cup/alchemy/alchemy_input.txt");
        // merge stones
        for (int i = 1; i <= caseNumbers; i++) {
            mergeStones(lengths.get(i - 1), colors.get(i - 1));
            colorName = new LinkedList<>();
            output.put(i, merged);
            merged = null;
            insertIndex = 0;
        }
        fileWrite(output);
    }

    private static void readDataFromFIle(String filepath) {
        Path filePath = Paths.get(filepath);
        List<String> data = null;
        try {
            data = Files.readAllLines(filePath);
            caseNumbers = Integer.parseInt(data.get(0));
            if (!(1 <= caseNumbers && caseNumbers <= 95)) {
                throw new IllegalAccessException("case numbers should be in range from 1 to 95");
            }
            for (int i = 1; i <= data.size() - 1; i++) {
                if (data.get(i).length() > 0) {
                    if (i % 2 == 0) {
                        colors.add(data.get(i));
                    } else {
                        lengths.add(Integer.parseInt(data.get(i)));
                    }
                }
            }
        } catch (IOException | IllegalAccessException e) {
            logger.log(Level.WARNING, e.getMessage());
        }
    }

    private static void mergeStones(int length, String color) {
        if ((3 <= length && length <= 99999) && (0 != length % 2)) {
            char[] colors = color.toCharArray();
//            logger.log(Level.INFO, "data from input length : " + length + " colors: " + color);
            for (char value : colors) {
                colorName.add(value);
            }

            while (colorName.size() >= 3) {
                List<Character> three = selectThreeForMerge();
                if (three.size() < 3) {
                    break;
                }

                char result = mergeStone(three);
                if (result == 0) {
                    break;
                } else if (colorName.size() == 0) {
                    break;
                }
                colorName.add(insertIndex, result);
            }
        } else {
            throw new IllegalArgumentException("length should be odd and be in range from 3 to 99,999");
        }
    }

    private static List<Character> selectThreeForMerge() {
        char tmp = 0;
        int equal = 0;
        LinkedList<Character> cloned = (LinkedList<Character>) colorName.clone();

        tmp = cloned.getFirst();
        equal = 1;
        List<Character> result = new ArrayList<>();
        result.add(tmp);
        if (cloned.stream().distinct().count() == 1) {
            merged = "N";
            return new ArrayList<>();
        }
        cloned.removeFirst();

        while (result.size() < 3) {
            for (int i = 0; i < cloned.size(); i++) {
                char stone = cloned.get(i);

                if (tmp != stone) {
                    tmp = stone;
                    result.add(stone);
                    cloned.remove(i);
                    if (i > 0) {
                        insertIndex = i;
                    }
                    break;
                } else {
                    equal++;
                    if (equal < 3) {
                        result.add(stone);
                        cloned.remove(i);
                        if (i > 0) {
                            insertIndex = i;
                        }
                        break;
                    }
                }
            }
        }
        colorName = cloned;
        return result;
    }

    private static char mergeStone(List<Character> elem) {
        int indexA = 0;
        int indexB = 0;
        char result = 0;
        for (char el : elem) {
            if ('A' == el) {
                indexA++;
            } else if ('B' == el) {
                indexB++;
            }
        }

        if (indexA > indexB) {
            merged = "Y";
            result = 'A';
        } else {
            merged = "Y";
            result = 'B';
        }
        return result;
    }

    private static void fileWrite(HashMap<Integer, String> result) {
        // for Java 1.7 or higher, FileWriter implements Closeable....
        String message;
        String path = "src/facebookhacker/cup/alchemy/output.txt";

        try (FileWriter writer = new FileWriter(path, false);) {
            for (int i : result.keySet()) {
                message = String.format("Case #%d: %s", i, result.get(i));
                logger.log(Level.INFO, message);

                writer.write(message);
                writer.append("\n");
            }
        } catch (IOException e) {
            logger.log(Level.WARNING, e.getMessage());
        }
    }
}
