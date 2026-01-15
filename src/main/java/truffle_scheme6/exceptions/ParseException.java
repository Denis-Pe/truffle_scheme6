package truffle_scheme6.exceptions;

public class ParseException extends RuntimeException {
    public ParseException(long index, long line, long column, String text) {
        super("Parse error at index " + index + ", line " + line + ", column " + column + ": " + text);
    }
}
