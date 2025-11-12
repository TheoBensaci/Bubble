# DOC AND DESIGN


## Game core

### Entity
#### Group
For many raison, separate entity in sup group can be useful, tipicly if you want to make some entity interact only with a set of entity with out making a new game instance.
Entity group aim to resolve that.
It makes sure only entity with the same group can collide with each other
And you can also render only a group of entity

#### Tag
It's use to make group interaction together without checking if a entity is instance of a mutitude of class
Like for example, imagine we have a multitude of way to kill a player, and we dont want to make a every entity a member of a "KillBox" class.
We can just add a tag "KillBox" and check tag on collision to know if we need to be kill, simple.




## Resource
All resource use to make this project (all blog, doc, media, paper, etc...)

### Network architecture
- https://github.com/0xFA11/MultiplayerNetworkingResources
- https://gafferongames.com/post/what_every_programmer_needs_to_know_about_game_networking/
- https://developer.valvesoftware.com/wiki/Latency_Compensating_Methods_in_Client/Server_In-game_Protocol_Design_and_Optimization
- https://www.gabrielgambetta.com/client-server-game-architecture.html

### Java 
- AWT : https://docs.oracle.com/javase/7/docs/api/java/awt/package-summary.html
- Image IO : https://docs.oracle.com/javase/8/docs/api/javax/imageio/ImageIO.html

### Oder 
- Knowledge from me (@TheoBensaci), i can't recall where i learn it, sry.

### AI usage
No AI as been use to make this project :]
