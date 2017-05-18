# Changelog
## Version 1.1.0
- Fixed a bug with the method `SimpleImage.rotate(double,Dimension)` that would calculate the wrong center of rotation.
- Fixed a bug that mirrored an image when rotating.
- Added the method `SimpleImage.show()` as you cannot just print out an image on the command line in order to see how it looks like.
- Added interfaces for scale and rotation algorithms. (causes incompatibility with older versions)
- Gave all filters an abstract super class. (causes incompatibility with older versions)
- Added clipboard functionality
- Made the error detection stricter.
- Added more overloaded methods to the `SimpleImage` class.
- Added an atlas system.
- Made it possible to filter only certain regions of an image.
- It is now possible to specify the algorithm to use for rotation.
- Added bilinear image rotation algorithm.

## Version 1.0.0
- Release