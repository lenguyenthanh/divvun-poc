package divvun

import org.omegat.core.Core
import org.omegat.core.data.SourceTextEntry
import org.omegat.gui.editor.UnderlineFactory.WaveUnderline
import org.omegat.gui.editor.mark.IMarker
import org.omegat.gui.editor.mark.Mark
import org.omegat.tokenizer.ITokenizer
import org.omegat.util.Token
import org.omegat.util.gui.Styles
import java.util.stream.Collectors
import java.util.stream.Stream
import javax.swing.text.Highlighter.HighlightPainter

class DivvunMarker : IMarker {

    private val dict = Dict()

    @Throws(Exception::class)
    override fun getMarksForEntry(
        ste: SourceTextEntry,
        sourceText: String?,
        translationText: String?,
        isActive: Boolean
    ): List<Mark> {

        println("Divvun Marker works: ${ste.key}")
        println("Divvun Marker works: $sourceText")
        println("Divvun Marker works: $translationText")
        println("Divvun Marker works: $isActive")

        if (translationText == null) {
            // translation not displayed
//            println("Divvun Marker works translationText == null")
            return emptyList()
        }
        if (!Core.getEditor().settings.isAutoSpellChecking) {
            // spell checker disabled
//            println("Divvun ction CommentByLineCommentMarker works spell checker disabled")
            return emptyList()
        }
        val tokens = Core.getProject().targetTokenizer.tokenizeWords(
            translationText,
            ITokenizer.StemmingMode.NONE
        )
        println("Divvun Marker tokens: " + tokens.size)
        return Stream.of(
            *Core.getProject().targetTokenizer.tokenizeWords(
                translationText,
                ITokenizer.StemmingMode.NONE
            )
        ).filter {tok: Token ->
            !dict.isCorrect(tok.getTextFromString(translationText))
        }
            .map { tok: Token ->
                val st = tok.offset
                val en = st + tok.length
                val m = Mark(Mark.ENTRY_PART.TRANSLATION, st, en)
                m.painter = PAINTER
                m
            }.collect(Collectors.toList())
    }

    companion object {
        protected val PAINTER: HighlightPainter =
            WaveUnderline(Styles.EditorColor.COLOR_SPELLCHECK.color)
    }
}