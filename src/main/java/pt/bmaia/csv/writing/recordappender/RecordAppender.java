package pt.bmaia.csv.writing.recordappender;

import pt.procurainterna.lang.text.charsoutput.CharsOutput;

public interface RecordAppender<R> {

  void writeTo(final R record, final CharsOutput appendable);

}
