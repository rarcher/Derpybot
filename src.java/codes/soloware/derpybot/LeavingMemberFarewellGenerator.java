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

import net.dv8tion.jda.core.events.guild.member.GuildMemberLeaveEvent;
import net.dv8tion.jda.core.hooks.EventListener;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
/**
 * A farewell protocol that triggers when people leave the server, in the form of an {@link EventListener}.
 */
public class LeavingMemberFarewellGenerator extends ListenerAdapter
{
    public LeavingMemberFarewellGenerator()
    {
    }

    @Override
    public void onGuildMemberLeave(final GuildMemberLeaveEvent event)
    {
        if (event.getGuild().getDefaultChannel().canTalk()&&(!event.getMember().getUser().isBot()))
        {
            event.getGuild().getDefaultChannel()
                    .sendMessage("Ohhh, "+event.getMember().getEffectiveName()+" left. Now I'm sad :sob:").complete();
        }
    }
}
