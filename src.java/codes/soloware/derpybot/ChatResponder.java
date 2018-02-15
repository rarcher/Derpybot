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

import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.EventListener;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
/**
 * An implementation of chatbot behavior, in the form of an {@link EventListener}. Feeds text chat input into a given
 * {@link TextGenerator}, and {@link TextGenerator#respond}s whenever a given keyword appears.
 */
public class ChatResponder extends ListenerAdapter
{
    private final String responseTrigger;
    private final TextGenerator responseGenerator;

    public ChatResponder(final String responseTrigger, final TextGenerator responseGenerator)
    {
        if (responseTrigger==null)
            throw new NullPointerException("Given response trigger keyword is null.");
        if (responseGenerator==null)
            throw new NullPointerException("Given response generator implementation is null.");
        this.responseTrigger=responseTrigger;
        this.responseGenerator=responseGenerator;
    }

    @Override
    public void onGuildMessageReceived(final GuildMessageReceivedEvent event)
    {
        if (event.getAuthor().equals(event.getJDA().getSelfUser()))
            return;
        if (event.getChannel().canTalk()&&event.getMessage().getContentDisplay().contains(responseTrigger))
            event.getChannel().sendMessage(responseGenerator.respond(event.getMessage().getContentDisplay())).complete();
        else responseGenerator.listen(event.getMessage().getContentDisplay());
    }
}
