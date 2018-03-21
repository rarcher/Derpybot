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

import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.hooks.EventListener;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
/**
 * A greeting protocol that triggers when new people join the server, in the form of an {@link EventListener}.
 */
public class NewMemberGreeter extends ListenerAdapter
{
    public NewMemberGreeter()
    {
    }

    @Override
    public void onGuildMemberJoin(final GuildMemberJoinEvent event)
    {
        if (event.getGuild().getDefaultChannel().canTalk()&&(!event.getMember().getUser().isBot()))
        {
            event.getGuild().getDefaultChannel()
                    .sendMessage("A new person! Hiya "+event.getMember().getAsMention()+"!").complete();
        }
    }
}
