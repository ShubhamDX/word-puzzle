# word-puzzle

This project is a websocket based real time word-puzzle game.

## Specifications:
1. STOMP messaging with SpringBoot has been used to create an interactive web application.
2. For testing purpose, basic UI has been developed from the sample boiler plate code from :
   https://spring.io/guides/gs/messaging-stomp-websocket/
   
   
## Steps:
1. Deploy the application. Access UI on web browser(with javascript enabled) at 8080

###### Header
2. Click the Connect button to establish a web socket connection
3. Enter your playerId(String) and click Register.

###### Notifications
4. You will be getting all the real time event notifications under the Notifications section

###### Game Information :
5. AllGameIds button lists all the game Ids.
6. ActiveGames button lists all the active games which are being played.
7. JoinTheseGames button gives you list of gameIds which are yet to be started and you can join these games to play.
8. GameInfo input text give you complete information about a particular gameId

###### Create Game :
9. If you want to create game, enter max number of players you want to join(mandatory) and your playerId. click Create.
10. The creator of the game is the admin of the game.

###### Join Game :
11. Players can join the game until maxPlayers of the particular game is breached.
12. To join a game : the maxPlayers limit shouldn't be breached and the game should be joinable(i.e. in yet to be started state )

###### Start Game : 
13. The game can be started in 2 ways. Either the admin can manually start the game or the game automatically starts once the max numbers of players have joined.

###### Play Game : 
14. Players can play the games as per their respective turns(which can be got by using the GetGameInfo search as in point 8 above)

###### Pass Chance
15. If a player passes his chance,he is automatically out of the game. A game stops in 2 cases : 
a)  All the players have passed
b)  All the contained words have exhausted
A leaderboard is shown once the game is over.

Notes: 
1. It is also possible that there may be some new words formed unintentionally due to random filling of the grid. If a player finds them,then also he/she is awarded.
2. Single letter words are not supported.
