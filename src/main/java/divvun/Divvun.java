package divvun;


/**************************************************************************
 OmegaT - Computer Assisted Translation (CAT) tool
 with fuzzy matching, translation memory, keyword search,
 glossaries, and translation leveraging into updated projects.

 Copyright (C) 2000-2006 Keith Godfrey and Maxym Mykhalchuk
 2010 Volker Berlin
 Home page: http://www.omegat.org/
 Support center: http://groups.yahoo.com/group/OmegaT/

 This file is part of OmegaT.

 OmegaT is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 OmegaT is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program.  If not, see <http://www.gnu.org/licenses/>.
 **************************************************************************/


import org.omegat.core.Core;
import org.omegat.core.CoreEvents;
import org.omegat.core.events.IApplicationEventListener;
import org.omegat.gui.editor.EditorController;


public class Divvun {

    /**
     * Plugin loader.
     */
    public static void loadPlugins() {
        Core.registerMarkerClass(divvun.DivvunMarker.class);
        CoreEvents.registerApplicationEventListener(generateIApplicationEventListener());
        Dict.Companion.bla();
    }

    private static IApplicationEventListener generateIApplicationEventListener() {
        return new IApplicationEventListener() {

            @Override
            public void onApplicationStartup() {
                EditorController editor = (EditorController) Core.getEditor();
                editor.registerPopupMenuConstructors(100, new SuggestionsPopUp(editor));
            }

            @Override
            public void onApplicationShutdown() {
            }
        };
    }
}