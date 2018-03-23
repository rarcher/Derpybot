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
import java.util.List;

import javax.security.auth.login.LoginException;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageHistory;
import net.dv8tion.jda.core.entities.TextChannel;
/**
 * Startup class of the Derpy chatbot.
 */
public class Derpy
{
    private static final Logger logger=LoggerFactory.getLogger(Derpy.class);
    private static final String tokenFileName="token.txt";
    private static final int maxMessagesRetrievablePerCall=100;

    public static void main(final String[] args)
    {
        final JDABuilder builder=new JDABuilder(AccountType.BOT);
        final TextGenerator engine=new LinkIgnoringTextGenerator(new EmoteIgnoringTextGenerator(new MarkovChain()));
        builder.addEventListener(new ChatResponder("Derpy", engine));
        builder.addEventListener(new NewMemberGreeter());
        builder.addEventListener(new LeavingMemberFarewellGenerator());

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

        final JDA accessToken;
        try
        {
            accessToken=builder.buildBlocking();
        }
        catch (final RuntimeException badToken)
        {
            logger.error("The contents of the token file \"{}\" are not a valid Discord token.", tokenFile.getFile(), badToken);
            return;
        }
        catch (final InterruptedException interrupted)
        {
            logger.error("Interrupted by another thread while attempting to log into Discord.", interrupted);
            return;
        }
        catch (final LoginException loginFailed)
        {
            logger.error("Discord login failed.", loginFailed);
            return;
        }

        for (final Guild server : accessToken.getGuilds())
        {
            for (final TextChannel channel : server.getTextChannels())
            {
                if (!channel.canTalk())
                    continue;
                final MessageHistory history=channel.getHistory();
                List<Message> trainingData=history.retrievePast(maxMessagesRetrievablePerCall).complete();
                while (trainingData.size()>0)
                {
                    for (final Message trainingDataEntry : trainingData)
                    {
                        if (!trainingDataEntry.getAuthor().equals(accessToken.getSelfUser()))
                            engine.listen(trainingDataEntry.getContentDisplay());
                    }
                    trainingData=history.retrievePast(maxMessagesRetrievablePerCall).complete();
                }
            }
        }
    }
}
