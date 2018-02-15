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

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.requests.restaction.MessageAction;

import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
/**
 * A set of test cases for the {ChatResponder} class.
 */
@RunWith(MockitoJUnitRunner.StrictStubs.class)
public class ChatResponderTest
{
    private static final String testResponseTrigger=" should ";
    private static final String responseTriggeringMockMessageContent="The testee should respond to this.";
    private static final String nonResponseTriggeringMockMessageContent="The testee shouldn't respond to this.";
    private static final String mockResponse="This is the response string.";
    private Message mockMessage;
    private MessageAction mockMessageAction;
    private User mockAuthor;
    private TextChannel mockChannel;
    private JDA mockJDA;
    private GuildMessageReceivedEvent mockEvent;
    private TextGenerator mockGenerator;

    @Before
    @SuppressWarnings("boxing")
    public void setup()
    {
        mockMessage=Mockito.mock(Message.class);

        mockMessageAction=Mockito.mock(MessageAction.class);

        mockAuthor=Mockito.mock(User.class);

        mockChannel=Mockito.mock(TextChannel.class);
        Mockito.when(mockChannel.canTalk()).thenReturn(true);
        Mockito.when(mockChannel.sendMessage(ArgumentMatchers.anyString())).thenReturn(mockMessageAction);

        mockJDA=Mockito.mock(JDA.class);
        Mockito.when(mockJDA.getAccountType()).thenReturn(AccountType.BOT);
        Mockito.when(mockJDA.getSelfUser()).thenReturn(null);

        mockEvent=Mockito.mock(GuildMessageReceivedEvent.class);
        Mockito.when(mockEvent.getMessage()).thenReturn(mockMessage);
        Mockito.when(mockEvent.getAuthor()).thenReturn(mockAuthor);
        Mockito.when(mockEvent.getChannel()).thenReturn(mockChannel);
        Mockito.when(mockEvent.getJDA()).thenReturn(mockJDA);

        mockGenerator=Mockito.mock(TextGenerator.class);
        Mockito.when(mockGenerator.respond(ArgumentMatchers.anyString())).thenReturn(mockResponse);
    }

    @Test
    public void listen()
    {
        Mockito.when(mockMessage.getContentDisplay()).thenReturn(nonResponseTriggeringMockMessageContent);

        new ChatResponder(testResponseTrigger, mockGenerator).onEvent(mockEvent);
        Mockito.verify(mockGenerator).listen(nonResponseTriggeringMockMessageContent);
    }

    @Test
    public void respond()
    {
        Mockito.when(mockMessage.getContentDisplay()).thenReturn(responseTriggeringMockMessageContent);

        new ChatResponder(testResponseTrigger, mockGenerator).onEvent(mockEvent);
        Mockito.verify(mockGenerator).respond(responseTriggeringMockMessageContent);
        Mockito.verify(mockChannel).sendMessage(mockResponse);
    }
}
