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
 * A {@link TextGenerator} decorator that filters links out of its training data. Links can't really be broken up into
 * recognizable words, so attaching this decorator to a <code>TextGenerator</code> can improve its output.
 */
public class LinkIgnoringTextGenerator implements TextGenerator
{
    private final TextGenerator base;

    public LinkIgnoringTextGenerator(final TextGenerator base)
    {
        if (base==null)
            throw new NullPointerException("Given base text generator is null.");
        this.base=base;
    }

    @Override
    public void listen(final String text)
    {
        if (!(text.contains("http://")||text.contains("https://")))
            base.listen(text);
    }

    @Override
    public String generate()
    {
        return base.generate();
    }

    @Override
    public String respond(final String text)
    {
        if (text.contains("http://")||text.contains("https://"))
            return base.generate();
        return base.respond(text);
    }
}
