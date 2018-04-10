# Derpy Bot

Derpy is a simple chatbot for Discord. She replies whenever someone says her user name in a channel she can access,
using Markov chains to generate novel responses from old chat logs. When deployed in a channel with a substantial chat
history, her responses can get *quite* amusing.

## Running the Bot

Derpy requires [Java](https://java.com/en/download/index.html) 1.8 or higher to work. Make sure you have it installed on
your machine before proceeding.

1. Download the [latest release](../../releases/latest).
2. Unzip the contents and put them anywhere you want.
3. Go to [the Discord applications page](https://discordapp.com/developers/applications/me).
4. Click `New App`.
5. Give your bot instance a name. This is the name the bot will have within Discord.
6. Click `Create App`.
7. Click `Create a Bot User`.
8. Click `Yes, do it!`.
9. Enter the following URL in a new browser tab, where `YOURCLIENTID` is the `Client ID` string listed on the app page
you created in step 6:
   ```
   https://discordapp.com/oauth2/authorize?client_id=YOURCLIENTID&scope=bot
   ```
10. Under `Select a server`, pick the server you want to deploy the bot on. You must have `Manage Server` permissions
for this to work.
11. Click `Authorize`.
12. Click the validation checkbox in the captcha that appears.
13. Go back to the app page you created in step 6.
14. Click `click to reveal`, next to `Token:`.
15. Copy the token string you revealed in step 14 into a text file named `token.txt`. Place it in the `config`
subdirectory of the bot installation you created in step 2.
16. Open the `bin` subdirectory of the bot installation you created in step 2.
17. Either double-click on `Derpy.jar`, or enter `java -jar Derpy.jar` in the console.
18. If any firewall notices appear, click `Allow`.

Your bot will now automatically connect to Discord, and should appear on your server shortly.
