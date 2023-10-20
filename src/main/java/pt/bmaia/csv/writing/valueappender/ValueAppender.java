package pt.bmaia.csv.writing.valueappender;


import pt.procurainterna.text.charsoutput.CharsOutput;

/**
 * Meant to perform transformations on individual textual values that are about to be placed on
 * a CSV output.
 * <p/>
 * Can be used to convert a textual value into a format mandated version. Applying escape sequences
 * to special characters, for example.
 */
public interface ValueAppender<V> {

  /**
   * Converts a textual value into a format to be appended to an output.
   *
   * @param charsOutput Something capable of receiving text.
   * @param value      The value to be converted and appended.
   */
  void append(CharsOutput charsOutput, V value);

}
