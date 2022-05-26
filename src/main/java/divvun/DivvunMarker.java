package divvun;


import org.omegat.core.Core;
import org.omegat.core.data.SourceTextEntry;
import org.omegat.gui.editor.UnderlineFactory;
import org.omegat.gui.editor.mark.IMarker;
import org.omegat.gui.editor.mark.Mark;
import org.omegat.tokenizer.ITokenizer;
import org.omegat.util.Token;
import org.omegat.util.gui.Styles;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.swing.text.Highlighter;

public class DivvunMarker implements IMarker {
    protected static final Highlighter.HighlightPainter PAINTER = new UnderlineFactory.WaveUnderline(Styles.EditorColor.COLOR_SPELLCHECK.getColor());

    @Override
    public List<Mark> getMarksForEntry(SourceTextEntry ste, String sourceText, String translationText, boolean isActive)
            throws Exception {
        System.out.println("Divvun Marker works: " + ste);
        System.out.println("Divvun Marker works: " + sourceText);
        System.out.println("Divvun Marker works: " + translationText);
        System.out.println("Divvun Marker works: " + isActive);
        if (translationText == null) {
            // translation not displayed
            System.out.println("Divvun Marker works translationText == null");
            return null;
        }
        if (!Core.getEditor().getSettings().isAutoSpellChecking()) {
            // spell checker disabled
            System.out.println("Divvun Marker works spell checker disabled");
            return null;
        }

        Token[] tokens = Core.getProject().getTargetTokenizer().tokenizeWords(translationText, ITokenizer.StemmingMode.NONE);
        System.out.println("Divvun Marker tokens: " + tokens.length);
//        for (Token token : tokens) {
//            System.out.println("token: " + token);
//        }
        return Stream.of(Core.getProject().getTargetTokenizer().tokenizeWords(translationText, ITokenizer.StemmingMode.NONE))
                .map(tok -> {
                    int st = tok.getOffset();
                    int en = st + tok.getLength();
                    Mark m = new Mark(Mark.ENTRY_PART.TRANSLATION, st, en);
                    m.painter = PAINTER;
                    return m;
                }).collect(Collectors.toList());
    }
}
