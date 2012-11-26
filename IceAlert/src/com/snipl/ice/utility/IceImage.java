package com.snipl.ice.utility;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class IceImage {

	public IceImage() {
	}
	
	private static Random random = new Random();


	public static BufferedImage resize(int i, int j, byte abyte0[]) {
		BufferedImage bufferedimage = null;
		ImageIcon imageicon = new ImageIcon(abyte0);
		Image image = imageicon.getImage();
		float f = image.getHeight(null);
		float f1 = image.getWidth(null);
		if (f < f1) {
			float f3 = (float) image.getHeight(null)
					/ (float) image.getWidth(null);
			j = (int) ((float) j * f3);
		} else {
			float f4 = (float) image.getWidth(null)
					/ (float) image.getHeight(null);
			i = (int) ((float) i * f4);
		}
		image = image.getScaledInstance(i, j, 4);
		bufferedimage = getBufferedImageFromImage(image);
		return bufferedimage;
	}

	public static byte[] resize(byte abyte0[], int i, int j) {
		InputStream inputStream = new ByteArrayInputStream(abyte0);
		try {
			BufferedImage image = ImageIO.read(inputStream);
			BufferedImage bufferedimage = resize(image, i, j);
			return convert(bufferedimage);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	public static BufferedImage resize(BufferedImage bufferedimage, int i, int j) {
		BufferedImage bufferedimage1 = null;
		byte abyte0[] = convert(bufferedimage);
		bufferedimage1 = resize(i, j, abyte0);
		return bufferedimage1;
	}

	public static BufferedImage getBufferedImageFromImage(Image image) {
		image = (new ImageIcon(image)).getImage();
		BufferedImage bufferedimage = new BufferedImage(image.getWidth(null),
				image.getHeight(null), 1);
		java.awt.Graphics2D graphics2d = bufferedimage.createGraphics();
		graphics2d.drawImage(image, 0, 0, null);
		graphics2d.dispose();
		return bufferedimage;
	}

	public static byte[] convert(BufferedImage bufferedimage) {
		ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream();
		try {
			ImageIO.write(bufferedimage, "JPEG", bytearrayoutputstream);
		} catch (IOException ioexception) {
			return new byte[0];
		}
		return bytearrayoutputstream.toByteArray();
	}
	
	public static BufferedImage getImageFromString(String string, int x, int y,
			int height, int width, boolean align) {

		char[] chars = new char[string.length()];
		string.getChars(0, string.length(), chars, 0);

		BufferedImage bufferedImage = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);

		Graphics2D g = bufferedImage.createGraphics();
		Font font = new Font("Arial", Font.BOLD, 20);
		if (font == null) {
			String path = Class.class.getResource("/").getPath();
			System.out.println(path);
			File fontFile = new File(path + "SOFTHITS.ttf");
			try {
				font = Font.createFont(Font.TRUETYPE_FONT, fontFile);
			} catch (FontFormatException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
//		Color color1 = new Color();
//		Color color2 = new Color();
		g.setBackground(Color.white);
		g.fill3DRect(0, 0, width, height, true);
		g.setColor(Color.MAGENTA);
		g.setFont(font);
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_RENDERING,
				RenderingHints.VALUE_RENDER_QUALITY);
		g.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION,
				RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
				RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_DITHERING,
				RenderingHints.VALUE_DITHER_ENABLE);
		g.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS,
				RenderingHints.VALUE_FRACTIONALMETRICS_ON);

		if (!align) {
			int xPoints[] = new int[chars.length];
			int yPoints[] = new int[chars.length];
			int nPoints = chars.length;
			for (int i = 0; i < chars.length; i++) {
				Font newFont = font.deriveFont(Font.BOLD, 20);
				AffineTransform affineTransform = newFont.getTransform();
				float angle = random.nextFloat();
				int sign = random.nextInt(2);
				angle = angle/2;
				if(i%2==0){
					g.setColor(Color.BLUE);
				}else{
					g.setColor(Color.BLUE);
				}
				if (sign == 1) {
					angle = -1 * angle;
				}
				affineTransform.rotate(angle);
				
				affineTransform.translate(affineTransform.getTranslateX(),
						affineTransform.getTranslateY() + angle * 15);
				newFont = newFont.deriveFont(affineTransform);
				g.setFont(newFont);
				xPoints[i] = (int) random.nextInt(width / (i + 1)
						+ random.nextInt(width / (i + 2))+random.nextInt(width / (i + 2)));
				xPoints[i] = (xPoints[i] < 0) ? xPoints[i] * -1 : xPoints[i];
				yPoints[i] = (int) random.nextInt(height);
				g.drawString(chars[i] + "", x + (i * 20), y);

			}
			g.setColor(Color.gray);
			g.drawPolyline(xPoints, yPoints, nPoints);
			

		} else {
			g.drawString(string, x, y);
		}
		g.dispose();
		return bufferedImage;
	}
	
	public static BufferedImage getImageFromString(String string, boolean align) {
		return getImageFromString(string, 15, 30, 50, 130, align);
	}


}