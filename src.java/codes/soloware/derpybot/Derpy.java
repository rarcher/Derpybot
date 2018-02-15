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

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.Charset;

import javax.security.auth.login.LoginException;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDABuilder;
/**
 * Startup class of the Derpy chatbot.
 */
public class Derpy
{
    private static final Logger logger=LoggerFactory.getLogger(Derpy.class);
    private static final String tokenFileName="token.txt";

    public static void main(final String[] args)
    {
        final JDABuilder builder=new JDABuilder(AccountType.BOT);
        builder.addEventListener(new ChatResponder("Derpy", new MarkovChain()));

        final URL tokenFile=Derpy.class.getClassLoader().getResource(tokenFileName);
        if (tokenFile==null)
        {
            logger.error("Discord token file \"{}\" not found. Please confirm it is on the classpath.", tokenFileName);
            return;
        }

        try (final InputStream tokenFileContents=tokenFile.openStream())
        {
            builder.setToken(IOUtils.toString(tokenFileContents, (Charset)null).trim());
        }
        catch (final IOException noTokenFile)
        {
            logger.error("Unable to read from Discord token file \"{}\".", tokenFile.getFile(), noTokenFile);
            return;
        }

        try
        {
            builder.buildBlocking();
        }
        catch (final RuntimeException badToken)
        {
            logger.error("The contents of the token file \"{}\" are not a valid Discord token.", tokenFile.getFile(), badToken);
        }
        catch (final InterruptedException interrupted)
        {
            logger.error("Interrupted by another thread while attempting to log into Discord.", interrupted);
        }
        catch (final LoginException loginFailed)
        {
            logger.error("Discord login failed.", loginFailed);
            return;
        }
    }
}