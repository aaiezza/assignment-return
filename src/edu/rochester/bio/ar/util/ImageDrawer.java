/**
 *  COPYRIGHT (C) 2017 Alex Aiezza. All Rights Reserved.
 */
package edu.rochester.bio.ar.util;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;

/**
 * @author Alex Aiezza
 *
 */
public class ImageDrawer
{

    public static void drawScaledImage( Image image, Component canvas, Graphics g )
    {
        int imgWidth = image.getWidth( canvas );
        int imgHeight = image.getHeight( canvas );

        double imgAspect = (double) imgHeight / imgWidth;

        int canvasWidth = canvas.getWidth();
        int canvasHeight = canvas.getHeight();

        double canvasAspect = (double) canvasHeight / canvasWidth;

        int x1 = 0; // top left X position
        int y1 = 0; // top left Y position
        int x2 = 0; // bottom right X position
        int y2 = 0; // bottom right Y position

        if ( imgWidth < canvasWidth && imgHeight < canvasHeight )
        {
            // the image is smaller than the canvas
            x1 = ( canvasWidth - imgWidth ) / 2;
            y1 = ( canvasHeight - imgHeight ) / 2;
            x2 = imgWidth + x1;
            y2 = imgHeight + y1;

        } else
        {
            if ( canvasAspect > imgAspect )
            {
                y1 = canvasHeight;
                // keep image aspect ratio
                canvasHeight = (int) ( canvasWidth * imgAspect );
                y1 = ( y1 - canvasHeight ) / 2;
            } else
            {
                x1 = canvasWidth;
                // keep image aspect ratio
                canvasWidth = (int) ( canvasHeight / imgAspect );
                x1 = ( x1 - canvasWidth ) / 2;
            }
            x2 = canvasWidth + x1;
            y2 = canvasHeight + y1;
        }

        g.drawImage( image.getScaledInstance( canvasWidth, canvasHeight, Image.SCALE_SMOOTH ), x1,
            y1, x2, y2, 0, 0, canvasWidth, canvasHeight, canvas );
    }
}
