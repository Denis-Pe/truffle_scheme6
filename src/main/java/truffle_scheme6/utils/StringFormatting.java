package truffle_scheme6.utils;

import java.util.Arrays;

public class StringFormatting {
    public static String spaced(Object[] objs) {
        return separatedBy(" ", objs);
    }

    public static String commaSeparated(Object[] objs) {
        return separatedBy(", ", objs);
    }

    public static String separatedBy(String separator, Object[] objs) {
        return Arrays.stream(objs)
                .map(Object::toString)
                .reduce((acc, next) -> acc + separator + next)
                .orElse("");
    }
}
