/*
 *  Copyright (C) 2018 Ryan Archer
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package codes.soloware.derpybot;
/**
 * An engine for generating native-language text responses to unstructured input.
 */
public interface TextGenerator
{
    /**
     * Use the given text to populate the engine, or to expand its response library, as appropriate.
     *
     * @param text      text to populate with
     */
    public void listen(String text);
    /**
     * Formulate and return a text string, using the generator's current response library.
     *
     * @return          generated text string
     */
    public String generate();
    /**
     * Formulate and return a response to the given text. This may or may not update the engine's response library,
     * depending on implementation.
     *
     * @param text      text to respond to
     * @return          an appropriate response
     */
    public default String respond(final String text)
    {
        listen(text);
        return generate();
    }
}
