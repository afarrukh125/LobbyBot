## Note
I hacked this project together really quickly over a total of about 4 hours. The code quality 
can certainly be improved. Happy to accept pull requests or suggestions.

## Discord Bot Idea (Lobbies/Temporary group chat)

When you are in a Discord server with a lot of people and you join a voice chat, you might send stuff to one 
another that you don’t want the entire server seeing, but you want the people in the voice chat that you are 
in seeing it. 

So if you send something, you might have to directly message it to everyone in the chat one by one. 

This discord bot aims to remove the hassle of doing all of that. 

When you join voice, you can ask the bot to setup a lobby. This will create a text chat that only you 
(and admins of the server) can see at first. When someone joins the voice chat, the bot sends a message 
in the private chat that the lobby owner can react to, to allow new people entering to see the chat. 
In this chat you can type away no problem.

## TODO
* Improve code quality - this was hacked together over a couple of days, reconsider the use of optionals
* Allow customisation of bot parameters, e.g. token (right now it uses the command line arguments)
* Move away from serialization. If the `Lobby` class is updated, then it might be necessary to delete
the `lobby.dat` file entirely, causing consistency issues.
* Base functionality largely complete, but might not function consistently.