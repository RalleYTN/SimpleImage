# Description
SimpleImage is a library that was written to simplify image processing in Java.
Complex processing can be done in a single line of code and is highly readable and maintainable.
The library was not optimized for game development! It is meant for basic GUI applications or command line tools.
The library supports the JavaFX framework. If support for JavaFX is **not** needed, just simply do not use the `simple-image-fx_vX.X.X.jar`.

## Why should I use SimpleImage? I mean, there already is `BufferedImage` and the Java2D API.
There are three reasons why you should use SimpleImage over `BufferedImage`.
The first reason is SimpleImage is easier to use. For example rotating an image can be done in a single line of code where everyone knows what it does.
With Java2D you need arround ten lines and it takes two looks before you know what it does.
The second reason is that SimpleImage has no overhead or things that are three times wrapped in objects.
You can directly work with primitive data types in a two dimensional integer array.
The third reason is that you do not have to worry about destroying the hardware acceleration as there is none.
The images are saved in RAM and will be processed by the CPU. This can be faster than `BufferedImage`.

## Features
- [x] Reading/Writing images
- [x] JavaFX support
- [x] Converting SimpleImage instances to BufferedImage instances and the other way arround
- [x] Fast image filtering
- [x] Pre-coded image filters
- [x] Mirroring images on both axis
- [x] Basic editing functions
- [x] Taking screenshots
- [x] Rotate/Scale/Combine/Mix images
- [x] Map images (spritesheets etc.)
- [x] Clipboard functionality

## Requirements
- Java 8 or higher

## Setup
Just put the `.jar` files on your class path.

# Links
See the [roadmap](https://trello.com/b/8oolmwiW)  
See the [online documentation](https://ralleytn.github.io/SimpleImage/)  
See the [changelog](https://github.com/RalleYTN/SimpleImage/blob/master/CHANGELOG.md)  
See the [download page](https://github.com/RalleYTN/SimpleImage/releases)
