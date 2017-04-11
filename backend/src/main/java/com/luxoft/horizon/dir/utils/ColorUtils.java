package com.luxoft.horizon.dir.utils;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Use to generate colors
 * @author rlapin
 */
public class ColorUtils {
    public static final String []COLOR_SCHEME = {"#063e61","#0a4f7a","#0d659c", "#367ba6", "#4794c4", "#60a6d2", "#7da8c3"};


    /**
     * Get <code>colors[count]</code> from Color scheme. If there is not enough colors reset counter
     * @param count
     * @return
     */
    public static List<String> getDifferentColors(int count){
        return IntStream.range(0,count).mapToObj((index)->COLOR_SCHEME[index%COLOR_SCHEME.length]).collect(Collectors.toList());
    }

    public static List<String> getSameColors(int count){
        return IntStream.range(0,count).mapToObj((index)->COLOR_SCHEME[0]).collect(Collectors.toList());
    }
}
