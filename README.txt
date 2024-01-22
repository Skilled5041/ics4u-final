Setup:
- Install the font aller/Aller_Std_Rg.ttf

Downloading Charts:
- Charts can be downloaded from here: https://quavergame.com/maps
- Not all of them will work with my game
- You must unzip the downloaded file (it is in the format of a .zip file)
- Move the extracted directory to the charts directory
- Convert the audio file in there to a .wav file. It must have the same name and end with .wav

Settings Information:
- Hit glow is the pink gradient that appears when you press A, S, K, L in game
- Lane separators are lines that separate the lanes
- Scroll speed is how fast the notes scroll down (the notes are the same, but they appear visually different). Lower scroll speed gives you more time to react, but the notes are denser and can be harder to read.
- Having a very slow scroll speed will cause notes to overlay, but even if they do not, it is still pretty much unplayable due to the note density and slow speed
- Offset is used if your audio has delay (e.g. with bluetooth headphones). A positive offset makes notes appear later (used when your audio is delayed).
- The colours of the notes of each lane can be customised

Notes:
- The game might look different depending on your operating system's scale
- Java 21 is used to make the game
- Do not modify or delete the settings file
- The skin can be customised by modifying the resources/skin directory
- At least one chart is required for the game to run properly.
- The notes in the hard charts may seem random, but they are synced to the song. However, you need to be VERY good at rhythm games to "feel" the sync.

Gameplay Info:
- The game is properly and fully synced to the music
- The game is played with the A, S, K, L keys
- You can exit mid game by pressing escape
- You can scroll through the maps using the scroll wheel or up/down arrow keys
- Click play or use space to play the selected map
- You can change the scroll speed in game by using the scroll wheel
- Accuracy is calculated by how many ms off you are from hitting a note perfectly
- You must time the initial timing and release of a long note (the release is more lenient)
- You are assigned a letter grade based on your accuracy
- You gain score when hitting notes
- You gain combo when hitting notes, it is reset when you miss a note
- Combo makes you gain slightly more score
- There are different accuracy classifications: miss, okay, good, great, perfect, marvelous
- When you hit a note, a pop-up will appear showing you accuracy
- By default, for perfect and marvelous, there will be no pop-up, so it is good when nothing appears
- The different accuracy classifications are based on your timing accuracy, and affect your accuracy and score differently
- Perfect and marvelous both give you 100% accuracy, but marvelous gives you more score
- The number in the bottom-middle of the screen is your combo
- The top-right has your grade, accuracy, and score
- The bar above the combo shows how accurate your timing is
- The arrow pointing to the bar shows how early or late you are (left is early, right is late, blue is best)
- The lines that show up on the bar are the timings of the notes
- The bar is used to help you time your notes
- Adjust your timing if you see that you are consistently hitting notes early or late
- There is a result screen after you finish the map
- You cannot fail a map


Bugs:
- Since I made it so that a long note disappears when released, you can see it very clearly disappear when releasing it very early. This is not noticeable when releasing it normally, but only when releasing it very early. This is intended behavior, but it is not ideal.
- You can cheese the game by spamming. This allows you to maintain combo and get score, but you get bad accuracy. I can fix this by adding a way to detect this, or a health system, but I do not have enough time to implement this.
- The song is sometimes offset after changing the scroll speed in the settings.

Missing functionality:
- I do not have multiplayer, but in my initial proposal, I said that it is not guaranteed.