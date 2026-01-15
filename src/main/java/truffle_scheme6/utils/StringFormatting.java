package truffle_scheme6.utils;

import java.util.Arrays;
import java.util.StringJoiner;

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

    public static String spaced(byte[] bytes) {
        return separatedBy(" ", bytes);
    }

    public static String separatedBy(String separator, byte[] bytes) {
        var joiner = new StringJoiner(separator);

        for (byte b : bytes) {
            joiner.add(String.valueOf(b));
        }

        return joiner.toString();
    }
}
