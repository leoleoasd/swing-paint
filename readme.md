# Swing-Paint: A poor copy of MS Paint using swing.

## Advantages:

1. Well-designed structure, can be easily extended. 
(For example, adding new painting pens and tools.)
2. Fully event-driven, paint objects by overriding the 
`paintComponent` method while other implementation only 
paint the drawing object to the canvas after the mouse released.
3. So, this application implemented the "Save image" 
and "Load image" functionality.
4. This is definitely not for any real life usage, but 
(In my opinion) good for education and for demonstrating swing usages.


## Structure:

### Tool

The first layer of abstraction is the `AbstractTool` class.
The selecting tool takes over the `MouseListener` and `MouseMotionListener` 
of the canvas component, controlling how to paint the canvas by implementing the `paint` method.

The `Tool` abstraction make it much easier for us to create a new tool, all you need to do is write 
the `MouseAdapter` of this tool and the `paint` method.

### Pen

The second layer of abstraction is the `AbstractPen` class. 
To make it easier to add a Painting Pen,
The `Pen` class only take care of how to generate a shape from a set of 
Points of Mouse motion.

### PenOption

The third layer of abstraction is `PenOption`, controls options of a pen, 
such as the Stroke and to fill it rather than drawing the outline of it.

## Painting progress

The canvas data stored in a BufferedImage, and at each time the canvas is repainting, 
the first thing we do is to draw the BufferedImage to the canvas, then we draw the painting
object to the canvas. (Painting means that the painting progress is not ended, for example, 
a drawing rectangle when the mouse hasn't released.) This can make drawing the preview of the 
object easier. After the painting progress ended, (Mouse released), the object is drew to the 
BufferedImage to store it predominately.

And at any time the BufferedImage is updated, we should make a copy of it and push it into the 
"undo stack" to provide the "undo and redo" functionality.

## What to do next.

One of the planed refactor is to rewrite the `EditTool` class, making it capable of rotating an image
and resizing it not only from lower right corner.

Any PR is appreciated.

## License.

This project is licensed under the GPL-V3 license with an additional requirement, 
you can found the GPL-V3 license [HERE](tree/master/LICENSE).

The additional requirement is that this project should not be used by anyone in their assignments 
or homework under any situation.
