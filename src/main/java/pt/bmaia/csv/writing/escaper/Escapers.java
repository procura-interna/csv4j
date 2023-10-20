package pt.bmaia.csv.writing.escaper;

public final class Escapers {

  private Escapers() {
  }

  public static Escaper fromChar(final char escapeChar) {
    return new CharEscaper(escapeChar);
  }

  public static Escaper fromCharArray(final char[] escapeCharArray) {
    return new CharArrayEscaper(escapeCharArray);
  }

  public static Escaper optimizedFromCharArray(final char[] escapeCharArray) {
    switch (escapeCharArray.length) {
      case 0:
        return new NotEscapingEscaper();

      case 1:
        return fromChar(escapeCharArray[0]);

      default:
        return fromCharArray(escapeCharArray);
    }
  }

}
