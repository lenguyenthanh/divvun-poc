package divvun;


import org.omegat.core.Core;
import org.omegat.core.spellchecker.SpellCheckerMarker;
import org.omegat.gui.editor.EditorController;
import org.omegat.gui.editor.IEditor;
import org.omegat.gui.editor.IPopupMenuConstructor;
import org.omegat.gui.editor.SegmentBuilder;
import org.omegat.tokenizer.ITokenizer;
import org.omegat.util.Log;
import org.omegat.util.OStrings;
import org.omegat.util.Token;
import org.omegat.util.gui.UIThreadsUtil;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.text.AbstractDocument;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;

public class SuggestionsPopUp implements IPopupMenuConstructor {
   protected final EditorController ec;

   public SuggestionsPopUp(EditorController ec) {
      this.ec = ec;
   }

   public void addItems(JPopupMenu menu, final JTextComponent comp, int mousepos,
                        boolean isInActiveEntry, boolean isInActiveTranslation, SegmentBuilder sb) {
      if (!ec.getSettings().isAutoSpellChecking()) {
         // spellchecker disabled
         return;
      }
      if (!isInActiveTranslation) {
         // there is no need to display suggestions
         return;
      }

      // Use the project's target tokenizer to determine the word that was right-clicked.
      // EditorUtils.getWordEnd() and getWordStart() use Java's built-in BreakIterator
      // under the hood, which leads to inconsistent results when compared to other spell-
      // checking functionality in OmegaT.
      String translation = ec.getCurrentTranslation();
      Token tok = null;
      int relOffset = ec.getPositionInEntryTranslation(mousepos);
      for (Token t : Core.getProject().getTargetTokenizer().tokenizeWords(translation, ITokenizer.StemmingMode.NONE)) {
         if (t.getOffset() <= relOffset && relOffset < t.getOffset() + t.getLength()) {
            tok = t;
            break;
         }
      }

      if (tok == null) {
         return;
      }

      final String word = tok.getTextFromString(translation);
      // The wordStart must be the absolute offset in the Editor document.
      final int wordStart = mousepos - relOffset + tok.getOffset();
      final int wordLength = tok.getLength();
      final AbstractDocument xlDoc = (AbstractDocument) comp.getDocument();

//      if (!Core.getSpellChecker().isCorrect(word)) {
      // todo divvun spell
      if (true) {
         // get the suggestions and create a menu
//         List<String> suggestions = Core.getSpellChecker().suggest(word);
         List<String> suggestions = Collections.singletonList("hello");

         // the suggestions
         for (final String replacement : suggestions) {
            JMenuItem item = menu.add(replacement);
            item.addActionListener(new ActionListener() {
               // the action: replace the word with the selected
               // suggestion
               public void actionPerformed(ActionEvent e) {
                  try {
                     int pos = comp.getCaretPosition();
                     xlDoc.replace(wordStart, wordLength, replacement, null);
                     comp.setCaretPosition(pos);
                  } catch (BadLocationException exc) {
                     Log.log(exc);
                  }
               }
            });
         }

         // what if no action is done?
         if (suggestions.isEmpty()) {
            JMenuItem item = menu.add(OStrings.getString("SC_NO_SUGGESTIONS"));
            item.addActionListener(new ActionListener() {
               public void actionPerformed(ActionEvent e) {
                  // just hide the menu
               }
            });
         }

         menu.addSeparator();

         // let us ignore it
         JMenuItem item = menu.add(OStrings.getString("SC_IGNORE_ALL"));
         item.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               addIgnoreWord(word, wordStart, false);
            }
         });

         // or add it to the dictionary
         item = menu.add(OStrings.getString("SC_ADD_TO_DICTIONARY"));
         item.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               addIgnoreWord(word, wordStart, true);
            }
         });

         menu.addSeparator();

      }
   }

   /**
    * add a new word to the spell checker or ignore a word
    *
    * @param word
    *            : the word in question
    * @param offset
    *            : the offset of the word in the editor
    * @param add
    *            : true for add, false for ignore
    */
   protected void addIgnoreWord(final String word, final int offset, final boolean add) {
      UIThreadsUtil.mustBeSwingThread();

      if (add) {
         Core.getSpellChecker().learnWord(word);
      } else {
         Core.getSpellChecker().ignoreWord(word);
      }

      ec.remarkOneMarker(SpellCheckerMarker.class.getName());
   }
}