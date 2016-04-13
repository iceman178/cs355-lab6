package cs355.model.image;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.awt.Color;
import java.util.Arrays;


public class Image extends CS355Image 
{
	public BufferedImage bufferedImage;
	private int[][] updatedPixels;
	
	public Image()
	{
		super();
		bufferedImage = null;
		updatedPixels = null;
	}
	
	private void setupUpdatedPixels()
	{
		if (updatedPixels == null) {
			updatedPixels = new int[super.getWidth() * super.getHeight()][3];
		}
	}

	private void updatePixels()
	{
		int height = super.getHeight();
		int width = super.getWidth();
		
		for (int y = 0; y < height; y++)
		{
			for (int x = 0; x < width; x++)
			{
				setPixel(x,y, updatedPixels[width * y + x]); 
			}
		}
	}
	
	
	@Override
	public BufferedImage getImage() 
	{
		if (bufferedImage != null) {
			return bufferedImage;
		}
		
		int w = super.getWidth();
		int h = super.getHeight();
		bufferedImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		WritableRaster wr = bufferedImage.getRaster();
		
		int[] rgb = new int[3];
		
		for (int y = 0; y < h; ++y) 
		{
			for (int x = 0; x < w; ++x) 
			{
				wr.setPixel(x, y, super.getPixel(x, y, rgb));
			}
		}
		
		bufferedImage.setData(wr);

		return bufferedImage;
	}

	@Override
	public void edgeDetection()
	{
		setupUpdatedPixels();
		
		int[] rgb = new int[3];
		float[] hsb = new float[3];
		
		int[] xPos = new int[9];
		int[] yPos = new int[9];
		
		int[] sX = {-1, 0, 1, -2, 0, 2, -1, 0, 1};
		int[] sY = {-1, -2, -1, 0, 0, 0, 1, 2, 1};
		
		int height = super.getHeight();
		int width = super.getWidth();
		
		for (int y = 0; y < height; ++y) 
		{
			for (int x = 0; x < width; ++x) 
			{
				int px = x > 0 ? x-1 : 0; //previous x, or 0
				int py = y > 0 ? y-1 : 0; //previous y, or 0
				int nx = x < width-1 ? x+1 : x; //next x, or x if edge
				int ny = y < height-1 ? y+1 : y; //next y, or y if edge
				
				xPos[0] = px; 	yPos[0] = py;
				xPos[1] = x; 	yPos[1] = py;
				xPos[2] = nx; 	yPos[2] = py;
				xPos[3] = px; 	yPos[3] = y;
				xPos[4] = x; 	yPos[4] = y;
				xPos[5] = nx; 	yPos[5] = y;
				xPos[6] = px; 	yPos[6] = ny;
				xPos[7] = x; 	yPos[7] = ny;
				xPos[8] = nx; 	yPos[8] = ny;
				
				double xTotal = 0;
				double yTotal = 0;
				
				for(int i = 0; i < 9; i++)
				{
					rgb = super.getPixel(xPos[i], yPos[i], rgb);
					hsb = Color.RGBtoHSB(rgb[0], rgb[1], rgb[2], hsb);
					
					xTotal += (sX[i]*hsb[2]);
					yTotal += (sY[i]*hsb[2]);
				}
				
				xTotal /= 8;
				yTotal /= 8;
				
				double magnitude = Math.sqrt(xTotal * xTotal + yTotal * yTotal);
				int colorValue = Math.min((int) ( magnitude * 255) + 128, 255);
				
				updatedPixels[width * y + x][0] = colorValue;
				updatedPixels[width * y + x][1] = colorValue;
				updatedPixels[width * y + x][2] = colorValue;
			}
		}
		
		updatePixels();
		bufferedImage = null;
	}

	@Override
	public void sharpen()
	{
		setupUpdatedPixels();
		
		int[] rgb = new int[3];
		
		int height = super.getHeight();
		int width = super.getWidth();
		
		for (int y = 0; y < height; y++) 
		{
			for (int x = 0; x < width; x++) 
			{
				int px = x > 0 ? x-1 : 0; // Previous x, or 0
				int py = y > 0 ? y-1 : 0; // Previous y, or 0
				int nx = x < width-1 ? x+1 : x; // Next x, or x if edge
				int ny = y < height-1 ? y+1 : y; // Next y, or y if edge
				
				rgb[0] = (-super.getRed(x, py)  +
						 -super.getRed(px, y)   +
						 (6*super.getRed(x, y)) +
						 -super.getRed(nx, y)   +
						 -super.getRed(x, ny)) / 2;
				
				rgb[1] = (-super.getGreen(x, py)  +
						 -super.getGreen(px, y)   +
						 (6*super.getGreen(x, y)) +
						 -super.getGreen(nx, y)   +
						 -super.getGreen(x, ny)) / 2;
				
				rgb[2] = (-super.getBlue(x, py)  +
						 -super.getBlue(px, y)   +
						 (6*super.getBlue(x, y)) +
						 -super.getBlue(nx, y)   +
						 -super.getBlue(x, ny)) / 2; 
				
				// Makes sure the value is within 0 <= x <= 255
				updatedPixels[width * y + x][0] = Math.max(Math.min(rgb[0], 255),0);
				updatedPixels[width * y + x][1] = Math.max(Math.min(rgb[1], 255),0);
				updatedPixels[width * y + x][2] = Math.max(Math.min(rgb[2], 255),0);
			}
		}
		
		updatePixels();
		bufferedImage = null;
	}

	@Override
	public void medianBlur()
	{
		setupUpdatedPixels();
		
		int height = super.getHeight();
		int width = super.getWidth();
		
		for (int y = 0; y < height; ++y) 
		{
			for (int x = 0; x < width; ++x) 
			{
				int px = x > 0 ? x-1 : 0; //previous x, or 0
				int py = y > 0 ? y-1 : 0; //previous y, or 0
				int nx = x < width-1 ? x+1 : x; //next x, or x if edge
				int ny = y < height-1 ? y+1 : y; //next y, or y if edge
				
				int[] red =	{super.getRed(px, py),
							super.getRed(x, py),
							super.getRed(nx, py),
							super.getRed(px, y),
							super.getRed(x, y),
							super.getRed(nx, y),
							super.getRed(px, ny),
							super.getRed(x, ny),
							super.getRed(nx, ny)};
				
				int[] green={super.getBlue(px, py),
							super.getBlue(x, py),
							super.getBlue(nx, py),
							super.getBlue(px, y),
							super.getBlue(x, y),
							super.getBlue(nx, y),
							super.getBlue(px, ny),
							super.getBlue(x, ny),
							super.getBlue(nx, ny)};
				
				int[] blue ={super.getGreen(px, py),
							super.getGreen(x, py),
							super.getGreen(nx, py),
							super.getGreen(px, y),
							super.getGreen(x, y),
							super.getGreen(nx, y),
							super.getGreen(px, ny),
							super.getGreen(x, ny),
							super.getGreen(nx, ny)};
				
				//order colors
				Arrays.sort(red);
				Arrays.sort(green);
				Arrays.sort(blue);
				
				//grab median color
				updatedPixels[width * y + x][0] = red[4];
				updatedPixels[width * y + x][1] = green[4];
				updatedPixels[width * y + x][2] = blue[4];
			}
		}
		
		updatePixels();
		bufferedImage = null;
	}

	@Override
	public void uniformBlur()
	{
		setupUpdatedPixels();
		
		int height = super.getHeight();
		int width = super.getWidth();
		
		for (int y = 0; y < height; ++y) 
		{
			for (int x = 0; x < width; ++x) 
			{
				int px = x > 0 ? x-1 : 0; //previous x, or 0
				int py = y > 0 ? y-1 : 0; //previous y, or 0
				int nx = x < width-1 ? x+1 : x; //next x, or x if edge
				int ny = y < height-1 ? y+1 : y; //next y, or y if edge
				
				//average red
				updatedPixels[width * y + x][0] =
							(super.getRed(px, py)+
							super.getRed(x, py)+
							super.getRed(nx, py)+
							super.getRed(px, y)+
							super.getRed(x, y)+
							super.getRed(nx, y)+
							super.getRed(px, ny)+
							super.getRed(x, ny)+
							super.getRed(nx, ny))/9;
				
				//average green
				updatedPixels[width * y + x][1] =
							(super.getBlue(px, py)+
							super.getBlue(x, py)+
							super.getBlue(nx, py)+
							super.getBlue(px, y)+
							super.getBlue(x, y)+
							super.getBlue(nx, y)+
							super.getBlue(px, ny)+
							super.getBlue(x, ny)+
							super.getBlue(nx, ny))/9;
				
				//average blue
				updatedPixels[width * y + x][2] =
							(super.getGreen(px, py)+
							super.getGreen(x, py)+
							super.getGreen(nx, py)+
							super.getGreen(px, y)+
							super.getGreen(x, y)+
							super.getGreen(nx, y)+
							super.getGreen(px, ny)+
							super.getGreen(x, ny)+
							super.getGreen(nx, ny))/9;
			}
		}
		
		updatePixels();
		bufferedImage = null;
	}

	@Override
	public void grayscale()
	{
		int[] rgb = new int[3];
		float[] hsb = new float[3];
		
		int height = super.getHeight();
		int width = super.getWidth();
		
		for (int y = 0; y < height; y++) 
		{
			for (int x = 0; x < width; x++) 
			{
				rgb = super.getPixel(x, y, rgb);
				
				hsb = Color.RGBtoHSB(rgb[0], rgb[1], rgb[2], hsb);
				
				hsb[1] = 0; //set saturation to 0
				
				Color c = Color.getHSBColor(hsb[0], hsb[1], hsb[2]);
				rgb[0] = c.getRed();
				rgb[1] = c.getGreen();
				rgb[2] = c.getBlue();

				setPixel(x, y, rgb);
			}
		}
		
		bufferedImage = null;
	}

	@Override
	public void contrast(int amount) 
	{
		float scalar = (float) Math.pow(((amount + 100.0f) / 100.0f), 4.0f);
        
		int[] rgb = new int[3];
		float[] hsb = new float[3];
		
		for (int y = 0; y < super.getHeight(); ++y) 
		{
			for (int x = 0; x < super.getWidth(); ++x) 
			{
				
				rgb = super.getPixel(x, y, rgb);
				
				hsb = Color.RGBtoHSB(rgb[0], rgb[1], rgb[2], hsb);
				
				hsb[2] = scalar*(hsb[2]-0.5f)+0.5f; //adjust brightness
				
				hsb[2] = Math.max(Math.min(hsb[2], 1.0f), 0.0f); //keep in range
				
				Color c = Color.getHSBColor(hsb[0], hsb[1], hsb[2]);
				
				rgb[0] = c.getRed();
				rgb[1] = c.getGreen();
				rgb[2] = c.getBlue();

				setPixel(x, y, rgb);
			}
		}
		
		bufferedImage = null;
	}

	@Override
	public void brightness(int amount) 
	{
		float adjustedAmount = amount/100.0f;
        
		int[] rgb = new int[3];
		float[] hsb = new float[3];
		
		for (int y = 0; y < super.getHeight(); ++y) 
		{
			for (int x = 0; x < super.getWidth(); ++x) 
			{
				
				rgb = super.getPixel(x, y, rgb);
				
				hsb = Color.RGBtoHSB(rgb[0], rgb[1], rgb[2], hsb);
				
				hsb[2] += adjustedAmount; //adjust brightness
				
				hsb[2] = Math.max(Math.min(hsb[2], 1.0f), 0.0f); //keep in range
				
				Color c = Color.getHSBColor(hsb[0], hsb[1], hsb[2]);
				
				rgb[0] = c.getRed();
				rgb[1] = c.getGreen();
				rgb[2] = c.getBlue();

				setPixel(x, y, rgb);
			}
		}
		
		bufferedImage = null;
	}
	
	
	
	
	
	
	
}
