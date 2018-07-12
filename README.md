# word-puzzle

This project is a websocket based real time word-puzzle game.

Specifications:
1. STOMP messaging with SpringBoot has been used to create an interactive web application.
2. For testing purpose, basic UI has been developed from the sample boiler plate code from :
   https://spring.io/guides/gs/messaging-stomp-websocket/
   
   
Steps to play:
1. Deploy the application. Access UI on web browser at 8080
2. Click the Connect button to establish a web socket connection
3. Enter your playerId(String) and click Register.
4. You will be getting all the real time event notifications under the Notifications section
----------------------------------------------------------
GameInformation Section:
5. AllGameIds button lists all the game Ids.
6. ActiveGames button lists all the active games which are being played.
7. JoinTheseGames button gives you list of gameIds which are yet to be started and you can join these games to play.
8. GameInfo input text give you complete information about a particular gameId
----------------------------------------------------------
CreateGameSection:
9. If you want to create game, enter max number of players you want to join(mandatory) and your playerId. click Create.
10. You are admin of this game.

