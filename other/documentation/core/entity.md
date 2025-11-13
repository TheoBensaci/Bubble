# Entity
Class use to manage basic entity behavior like Identification, grouping and tagging
### Group
For many raison, separate entity in sup group can be useful, tipicly if you want to make some entity interact only with a set of entity with out making a new game instance.
Entity group aim to resolve that.
It makes sure only entity with the same group can collide with each other
And you can also render only a group of entity

### Tag
It's use to make group interaction together without checking if a entity is instance of a mutitude of class
Like for example, imagine we have a multitude of way to kill a player, and we dont want to make a every entity a member of a "KillBox" class.
We can just add a tag "KillBox" and check tag on collision to know if we need to be kill, simple.
