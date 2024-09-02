package pt.bmaia.csv.writing.escaper;

import pt.procurainterna.lang.text.charsinput.CharsInput;
import pt.procurainterna.lang.text.charsoutput.CharsOutput;

public interface Escaper {

  void escape(CharsInput charsInput, CharsOutput charsOutput);

}
