package pt.bmaia.csv.writing.escaper;

public class StandardEscapeChecker implements EscapeChecker {

  private static final char CARRIAGE_RETURN = '\r';
  private static final char LINE_FEED = '\n';

  private final char escapeChar;

  public StandardEscapeChecker(final char escapeChar) {
    this.escapeChar = escapeChar;
  }

  @Override
  public boolean needsToBeEscaped(final char character) {
    return character == escapeChar ||  character == CARRIAGE_RETURN || character == LINE_FEED;
  }

}
