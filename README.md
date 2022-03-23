# Collidr
## Object collision detection using a QuadTree for maximum efficiency

### Reasoning
Collision detection gets increasingly less efficient the more objects need to be accounted for. Checking every object against every other object seems counter intuitive, even when there are very few objects to begin with. 

### The Solution
A more intuitive approach to collision detection is one to only check for collisions against objects which are nearby.

Using a QuadTree (a tree where each parent has 4 children), this program seperates the screen in increasingly small quadrants, each quadrant being a single node. When a certain set threshold of objects are located in one quadrant, the node dynamically splits into 4 children creating smaller quadrants.

Each object on screen is then only compared to objects in the same quadrant as itself. So while this doesn't change the time complexity of the detection algorithm, it largely decreases the size of the data set, resulting in faster calculations and less lag.

In the picture below, the balls that are currently colliding have their bounding box visible. The dividing lines are the boundaries of each node on the tree.

![demo](https://github.com/guymorgenshtern/Collidr/blob/5dd3f53eb4e5058ff6f99623bbd0d9391c16e2bd/Screenshot%202021-10-10%20at%2012.02.22%20PM.png)
