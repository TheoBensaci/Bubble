# Resource management
Resource are loaded in to a hash map, this aim to avoid reload a resource all ready load.

#### Step
1) When you ask for a resource, the first time, it will search it in the resource folder.
2) 1) Depending if we found it, the resource will be loaded in to the hash map
   2) if the resource happend to be missed, the entry on the hash map will be associate to a default resource
3) next time you try to load the resource, it will check the hash map and direcly send you the right resource with out using the file system
