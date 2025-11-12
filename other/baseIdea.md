We want to make a twin stick shooter (we're not gonna support controller, it's just the name a of the gender, we are talking mouse and keyboard).
But there is twist, limited space, our idea is to make a 1v1 twin stick shooter were the space it self is a resource you need to manage at best to win.

To explain, let's imagine a map :
░░○░░
░○░○░
░░○░░

(○ =bubble and ░ = void)

So, the game is 1v1, you player a space ship witch can :

    move in 8 direction (keyboard control)
    shoot in all direction (with the mouse), you have got 5 ammo and it reload it self when you don't shoot
    2 dash, witch can be use to avoid opponents bullet and move your self, it reload is self after a short time
    1 life, get hit and you'r dead >:[

All those is true, as long as you are in a bubbles , when you outside, no more movement, no more reload of your ammo, no more reload of you dash, the moment you'r outside bubbles, resource become limited.

Bubbles also act like a protection, bullet from inside the bubbles can pass throw, but not the other way around.

Unfortunately, bubbles are not eternal, when a player is inside of one, it shrink, and it to small, it's destroy it self. All so, if a player shoot a bubble, it will shrink to.

Destroyed bubbles are not gone forever, after a will, it will re spawn.

On top of that, as a player, you can charge a "super" shoot witch is bigger, can pass throw bubbles (even if it will normally be blocked).

So, how to you win ?
It's a sort of death match, every time you beat your opponent, all the bubbles will be refresh, you will be given 3 point and you opponent will re spawn, the 1st to have more then 50 point win.

But if the lobby have a more then 2 people, what happen ?
Simple, 3 -> it's a king of hill (and 1 player spectate will waiting one player got beat)
the loser is next to spectate
4 -> 2 map, the loser map and the winner map, when you'r on the loser and the win you got to the winner map, if your on the winner and win, you stay, if not you got to the loser, and you go to the loser and lose, you stay

So for N -> if N is even then it will be N/2 map. if N is odd, then the loser of the last map is put to spectate waiting a new loser.

Visually the game will have a GUI (Theo as made a prototype with AWT witch work, so it's all good)

but not for all, to reduce work weight, they will be no menu.
To connect to a lobby, launch our .jar with the lobby parameter and it will start the game on this lobby.

So all menu will be in bash.
They will be of course a server. Maybe we will include the server with jar and start the application as a sever or a client will be a option at launch.

And if i think that's all. I hope it's clear :]
("I" is Theo)