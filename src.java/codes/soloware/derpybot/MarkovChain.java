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

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.regex.Pattern;

import gnu.trove.map.hash.TObjectIntHashMap;
/**
 * An implementation of a Markov chain as applied to text. Tracks the relative probabilities of word combinations in
 * input, and uses them to generate novel responses.
 */
public class MarkovChain implements TextGenerator
{
    private final Pattern splitter;
    private final Random randomNumbers;
    private final Set<String> sentenceStarters;
    private final Map<String, TObjectIntHashMap<String>> wordPairOccurrenceTallies;

    public MarkovChain()
    {
        this(" ");
    }

    public MarkovChain(final String splitRegex)
    {
        this(Pattern.compile(splitRegex));
    }

    public MarkovChain(final Pattern splitter)
    {
        if (splitter==null)
            throw new NullPointerException("Given pattern for splitting input strings is null.");
        this.splitter=splitter;
        this.randomNumbers=new Random();
        this.randomNumbers.setSeed(System.currentTimeMillis());
        this.sentenceStarters=new LinkedHashSet<String>();
        this.wordPairOccurrenceTallies=new LinkedHashMap<String, TObjectIntHashMap<String>>();
    }

    @Override
    public final void listen(final String text)
    {
        final String[] splitText=splitter.split(text);
        sentenceStarters.add(splitText[0]);
        for (int loop=0; loop<splitText.length-1; loop++)
            incrementTally(splitText[loop], splitText[loop+1]);
        incrementTally(splitText[splitText.length-1], null);
    }

    private void incrementTally(final String firstWord, final String secondWord)
    {
        if (!wordPairOccurrenceTallies.containsKey(firstWord))
            wordPairOccurrenceTallies.put(firstWord, new TObjectIntHashMap<String>());
        if (!wordPairOccurrenceTallies.get(firstWord).contains(secondWord))
            wordPairOccurrenceTallies.get(firstWord).put(secondWord, 0);
        wordPairOccurrenceTallies.get(firstWord).increment(secondWord);
    }

    @Override
    public String respond(final String text)
    {
        listen(text);

        final StringBuilder response=new StringBuilder();
        String currentWord=startSentence();
        while (true)
        {
            response.append(currentWord);
            currentWord=pickNextWord(currentWord);
            if (currentWord==null)
                break;
            response.append(" ");
        }
        return response.toString();
    }

    private String startSentence()
    {
        int wordPosition=randomNumbers.nextInt(sentenceStarters.size());
        for (final String possibleWord : sentenceStarters)
        {
            if (wordPosition==0)
                return possibleWord;
            wordPosition--;
        }
        return null;
    }

    private String pickNextWord(final String currentWord)
    {
        int tallySum=0;
        for (int tally : wordPairOccurrenceTallies.get(currentWord).values())
            tallySum=tallySum+tally;

        int wordNumber=randomNumbers.nextInt(tallySum);
        for (final String possibleWord : wordPairOccurrenceTallies.get(currentWord).keySet())
        {
            wordNumber=wordNumber-wordPairOccurrenceTallies.get(currentWord).get(possibleWord);
            if (wordNumber<=0)
                return possibleWord;
        }
        return null;
    }
}
