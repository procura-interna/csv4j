package pt.bmaia.csv.writing.valueappender;

import pt.procurainterna.lang.text.charsoutput.CharsOutput;

public class NoOpValueAppender<V> implements ValueAppender<V> {

  @Override
  public void append(final CharsOutput charsOutput, final V value) {
    // Do nothing
  }

}
