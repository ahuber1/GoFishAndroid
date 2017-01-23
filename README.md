# Go Fish Android App

In the Spring 2012 semester, I made this Android app that would allow one to play the card game Go Fish on their Android device. In the app, the player can either play against a computer player, or you can pass the Android device around, and play with multiple people. You can also use what I call "accelerometer gestures", are movements that one can make with the Android device to perform a particular action. For more information on this, go to the [Gameplay Screen](### Gameplay Screen) section. This app received a departmental recognition award from the Computer Science department at my university.

The code itself is located in the `code` directory, screenshots of the app are located in the `screenshots` directory, and the `images` directory contains additional images that are used in this README. The screenshots are shown and described below.

## Screenshots

### Welcome Screen
This is the screen that asks who will play. First, Player 1 enters their name in the topmost text field. Next, the player selects  `Computer Game` or `Multiplayer Game` depending on the type of game that they would like to play. If the user selects `Multiplayer Game`, additional text fields appear where one can enter additional players' names. To start the game, press the `Start game!` button at the bottom right.

![WelcomeScreen.png cannot be found][welcome]

### Pairs Found Dialog
This is a dialogue that appears when pairs were found. In this instance, Andrew got lucky, and got three pairs when his hand was dealt at the beginning of the game.

![PairsFoundDialog.png cannot be found][pairs_found]

### Gameplay Screen

Below is an image of what one can see during gameplay. On the upper left are the other players' pairs; in this instance, this is a game between myself (Andrew) and the Computer player, and because it is my turn, only the Computer player is shown. This is also where you select a player to ask for a particular card.

On the right-hand side is a conversation thread that shows the dialogue that is occurring during gameplay.

* Anything labeled "The Game" is what is "spoken" by the game's moderator.
* Anything labeled "Computer" is what is "spoken" by the computer player.
* Anything labeled "Andrew" (not shown) is what is "spoken" by myself.

Do not interpret the word "spoken" literally; the conversation thread is merely a means to keep track of what each player has done.

On the bottom is a horizontal scroll view displaying your hand and your pairs. If you wish to ask for a particular card in your hand, select it, and press the button on the far left that, in this instance, is labeled `Ask Computer`. You may also select one of your pairs, and ask to see if a player has an additional card; in this instance, you could select the pair of 10s, and ask the computer player if it has an additional 10 to add to the pair.

![GameplayWindow.png cannot be found][gameplay]

#### Accelerometer Gestures
One of the unique aspects of this Android app is the ability to use what I call "accelerometer gestures." In this app there are two accelerometer gestures: _slam_ and _jolt_.

**Slam.** The user can move the device _like_ you are slamming a newspaper or magazine on the coffee table (please do not _literally_ slam your device on a coffee table). When this gesture is performed, a pop up asking if the user would like to restart the game is displayed. This gesture mimics someone slamming a paper on a table in frustration (i.e., "I give up! I want to start a new game.")

![slam.png cannot be found][slam]

**Jolt.** The user can jolt the device to the left or right (i.e., move the device quickly left or right). When this gesture is performed, a pop up asking if the user would like to return to the main menu is performed.

![jolt.png cannot be found][jolt]

### Winner Dialog

This dialog appears when someone wins the game.

![WinnerDialog.png cannot be found][winner]

[gameplay]: https://github.com/ahuber1/GoFishAndroid/blob/master/screenshots/GameplayWindow.png?raw=true "Gameplay Window"
[pairs_found]: https://github.com/ahuber1/GoFishAndroid/blob/master/screenshots/PairsFoundDialog.png?raw=true "Pairs Found Dialog"
[welcome]: https://github.com/ahuber1/GoFishAndroid/blob/master/screenshots/WelcomeScreen.png?raw=true "Welcome Screen"
[winner]: https://github.com/ahuber1/GoFishAndroid/blob/master/screenshots/WinnerDialog.png?raw=true "Winner Dialog"

[jolt]: https://github.com/ahuber1/GoFishAndroid/blob/master/images/jolt.png?raw=true "Jolting a device"
[slam]: https://github.com/ahuber1/GoFishAndroid/blob/master/images/jolt.png?raw=true "Slamming the device"
