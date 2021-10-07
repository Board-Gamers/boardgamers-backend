package com.a404.boardgamers.Util;

import java.util.ArrayList;
import java.util.List;

public class StringToList {
    public static List<String> parsing(String string) {
        List<String> result = new ArrayList<>();
        int from = string.indexOf("'", 0);
        int to = string.indexOf("'", from + 1);
        while (from != -1 && to != -1) {
            String piece = string.substring(from + 1, to);
            System.out.println(from + " " + to + ": " + piece);
            result.add(piece);
            from = string.indexOf("'", to + 1);
            to = string.indexOf("'", from + 1);
        }
        return result;
    }
}
