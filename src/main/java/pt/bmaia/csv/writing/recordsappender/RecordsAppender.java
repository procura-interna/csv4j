package pt.bmaia.csv.writing.recordsappender;

import pt.procurainterna.lang.text.charsoutput.CharsOutput;

public interface RecordsAppender<D> {

  void writeTo(D dataRecords, CharsOutput charsOutput);

}
