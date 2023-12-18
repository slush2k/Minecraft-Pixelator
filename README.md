# Minecraft Image Pixelator
__Description:__

The Minecraft Image Mapper is a Java program designed to transform source images into pixel art representations using a custom colour library based on Minecraft blocks. The program samples clusters of pixels in 16x16 areas from a source image calculates the average RGB colour for each cluster and builds an index of Minecraft blocks with their corresponding average colours. Utilizing a KD tree for efficient colour matching, the program then maps each pixel cluster from the source image to the closest matching Minecraft block colour. The resulting output generates pixel art resembling the source image, using Minecraft block textures to represent the colour composition of the original image.

__Key Features:__

Pixel clustering and average RGB colour extraction from source images.
Construction of a Minecraft block colour library based on averaged colours.
Implementation of a KD tree for efficient colour matching.
Generation of pixel art representations using Minecraft block textures.

__Target Audience:__

This project caters to Minecraft enthusiasts and artists who wish to convert images into Minecraft-style pixel art, providing a unique and creative way to integrate real-world images into the Minecraft environment.

__Technologies Used:__

Java
KD Tree algorithm
Minecraft block texture library

## Example
Source | Minecraft Block Representation
-------- |-----
<img src="https://github.com/slush2k/GitPixel/assets/40444049/e774a26a-97d2-44a8-8684-463f34e8b10f" height="400" >| <img src="https://github.com/slush2k/GitPixel/assets/40444049/31d78c05-d992-4037-8233-75c70e6c70f4" height="400">


