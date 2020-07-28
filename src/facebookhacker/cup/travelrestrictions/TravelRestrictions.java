package facebookhacker.cup.travelrestrictions;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


/**
 * created by Oleksandr Koryttsev
 * korytcev@gmail.com
 */
class TravelRestrictions {

    private static int airlines = 0;

    private static List<Integer> matrixSize = new ArrayList<>();

    private static List<String> incoming = new ArrayList<>();

    private static List<String> outcoming = new ArrayList<>();

    private static HashMap<Integer, Character[][]> result = new HashMap<>();

    private static Character[][] matrix = null;

    private static int syzeM = 0;

    private static char[] inc = null;

    private static char[] out = null;

    public static void main(String[] args) {
//        readDataFromFIle("src/facebookhacker/cup/travelrestrictions/travel_restrictions_validation_input.txt");
        readDataFromFIle("src/facebookhacker/cup/travelrestrictions/travel_restrictions_validation_input.txt");
        System.out.println(matrixSize);
        System.out.println(incoming);
        System.out.println(outcoming);
        fillMatrix(matrixSize, incoming, outcoming);
    }

    private static void fillMatrix(List<Integer> size, List<String> incoming, List<String> outcoming) {
        for (int i = 0; i <= airlines - 1; i++) {
            syzeM = size.get(i);
            inc = incoming.get(i).toCharArray();
            out = outcoming.get(i).toCharArray();
            Character[][] tmp = createMatrix(syzeM, inc, out);
            result.put(i, tmp);
        }

        printMatrix();
    }

    private static void printMatrix() {
        String path = "src/facebookhacker/cup/travelrestrictions/output.txt";

        String message;

        try (FileWriter writer = new FileWriter(path, false);) {
            for (int l : result.keySet()) {
                message = String.format("Case #%s: ", l + 1);
                System.out.println(message);
                writer.write(message);
                writer.append("\n");
                for (int i = 0; i <= matrixSize.get(l) - 1; i++) {
                    for (int j = 0; j <= matrixSize.get(l) - 1; j++) {
                        System.out.print(result.get(l)[i][j]);
                        writer.write(result.get(l)[i][j]);
                    }
                    System.out.println();
                    writer.append("\n");
                }
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }


    private static Character[][] createMatrix(int m, char[] inc, char[] out) {
        System.out.println("incoming flights:" + Arrays.toString(inc));
        System.out.println("outgoing flights:" + Arrays.toString(out));
        int indexToCompare = 0;
        char restrict = 0;
        matrix = new Character[m][m];
        for (int i = 0; i <= m - 1; i++) {

            for (int j = 0; j <= m - 1; j++) {
                if (i == j) {
                    matrix[i][j] = 'Y';
                } else if ((i < j && j - 1 == i && out[i] == 'Y' && inc[j] == 'Y') || (i > 0 && i - 1 == j
                                                                                       && out[i] == 'Y'
                                                                                       && inc[j] == 'Y'))
                {
                    matrix[i][j] = 'Y';
                } else {
                    matrix[i][j] = 'N';
                }
            }
        }
        return matrix;
    }

    private static void readDataFromFIle(String filepath) {
        Path filePath = Paths.get(filepath);
        List<String> data = null;
        try {
            data = Files.readAllLines(filePath);
            airlines = Integer.parseInt(data.get(0));
            data.remove(0);
            if (!(1 <= airlines && airlines <= 100)) {
                throw new IllegalAccessException("case numbers should be in range from 1 to 100");
            }
            while (data.size() > 0) {
                matrixSize.add(Integer.parseInt(data.get(0)));
                data.remove(0);
                incoming.add(data.get(0));
                data.remove(0);
                outcoming.add(data.get(0));
                data.remove(0);
            }
        } catch (IOException |

            IllegalAccessException e) {
            System.err.println(e.getMessage());
        }
    }
}
