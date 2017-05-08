package com.osfeng.cool.neural;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import javax.imageio.ImageIO;

public class ImgUtils {

	public static void img2Vector(double[] vector, String src){
		try {
			BufferedImage img = ImageIO.read(new File(src));
			int w = img.getWidth();
			int h = img.getHeight();
			if(vector.length!=w*h){
				vector = new double[vector.length];
				return ;
			}
			for (int i = 0; i < w; i++) {
				for (int j = 0; j < h; j++) {
					final int color = img.getRGB(i, j);
					
					final int r = (color >> 16) & 0xff;
					final int g = (color >> 8) & 0xff;
					final int b = color & 0xff;
					int gray = (int) (0.3 * r + 0.59 * g + 0.11 * b);
					if(gray >= 128){
						vector[i*img.getWidth()+j]=0;
					}else{
						vector[i*img.getWidth()+j]=1;
					}
//					vector[i*img.getWidth()+j]=gray;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println(src);
		}
	}
	
	public static void img2Vector(double[] vector, String src, int w, int h){
		try {
			BufferedImage img = ImageIO.read(new File(src));
			BufferedImage compressImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);	
			compressImg.getGraphics().drawImage(img.getScaledInstance(w, h, Image.SCALE_SMOOTH), 0, 0, null);
			if(vector.length!=w*h){
				vector = new double[vector.length];
				return ;
			}
			for (int i = 0; i < w; i++) {
				for (int j = 0; j < h; j++) {
					final int color = compressImg.getRGB(i, j);
					
					final int r = (color >> 16) & 0xff;
					final int g = (color >> 8) & 0xff;
					final int b = color & 0xff;
					int gray = (int) (0.3 * r + 0.59 * g + 0.11 * b);
					if(gray >= 128){
						vector[i*compressImg.getWidth()+j]=0;
					}else{
						vector[i*compressImg.getWidth()+j]=1;
					}
//					vector[i*img.getWidth()+j]=gray;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println(src);
		}
	}
	
	public static void main(String[] args) {
		String src = "/home/xuzhenmin/temp/test/data/0/0_2.bmp";
		double[] vector = new double[8*8];
		img2Vector(vector, src);
		System.out.println(Arrays.toString(vector));
	}
}
