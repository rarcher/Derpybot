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

import org.junit.Assert;
import org.junit.Test;
/**
 * A set of test cases for the {@MarkovChain} class.
 */
public class MarkovChainTest
{
	@Test
	@SuppressWarnings("static-method")
	public void listenSuccessfully()
	{
		new MarkovChain().listen("This is test input.");
	}

	@Test
	@SuppressWarnings("static-method")
	public void respondSuccessfully()
	{
		Assert.assertTrue(new MarkovChain().respond("This is test input.").equals("This is test input."));
	}
}
