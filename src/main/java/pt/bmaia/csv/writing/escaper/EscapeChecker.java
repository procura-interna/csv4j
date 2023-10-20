package pt.bmaia.csv.writing.escaper;

public interface EscapeChecker {

  boolean needsToBeEscaped(final char character);

}
