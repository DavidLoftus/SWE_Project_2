package scrabble.wordlist;

import java.io.IOException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

//import net.sourceforge.sizeof;

public class UseGaddag {
    public static void main(String[] args) throws IOException {
        Sowpods sowpods = new Sowpods();
        Set<String> smallerSet = new HashSet<>();

        for (String s : sowpods.getWordList()) {
            if (s.length() <= 8) {
                smallerSet.add(s);
            }
        }

        Gaddag gaddag = new Gaddag(smallerSet);
//        System.out.println(SizeOf.deepSizeOf(gaddag));

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String word = scanner.next();
            System.out.println(gaddag.complete(word));
        }

    }
}
