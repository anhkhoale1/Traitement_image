package projetinit;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import javax.imageio.ImageIO;

public class ImgTraitement {
	
	/**
	 * Pour lire une image à partir d'un fichier
	 * @param file : le fichier de l'image
	 * @return l'image
	 * @throws Exception
	 */
	public static BufferedImage ImgRead(File file) throws Exception {
		BufferedImage image = null;
		try {
			image = ImageIO.read(file);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return image;
	}

	/**
	 * Récupère les données d'une image
	 * @param filename : le nom du fichier de l'image
	 * @return les données de l'image dans un tableau de byte
	 */
	public static byte[] getImgData1(String filename) {
		BufferedImage srcImage = null;
		try {
			File imgFile = new File(filename);
			srcImage = javax.imageio.ImageIO.read(imgFile);
		} catch (IOException ioE) {
			System.err.println(ioE);
			System.exit(1);
		}
		Raster raster = srcImage.getData();
		DataBuffer buffer = raster.getDataBuffer();

		int type = buffer.getDataType();
		if (type != DataBuffer.TYPE_BYTE) {
			System.err.println("Wrong image data type");
			System.exit(1);
		}
		if (buffer.getNumBanks() != 1) {
			System.err.println("Wrong image data format");
			System.exit(1);
		}
		DataBufferByte byteBuffer = (DataBufferByte) buffer;
		byte[] srcData = byteBuffer.getData(0);
		return srcData;
	}
	
	

	/**
	 * Normalise l'image
	 * @param image : image à normaliser
	 * @return l'image normalisée
	 */
	public static BufferedImage normalize_minmax(BufferedImage image) {
		int rows = image.getHeight();
		int cols = image.getWidth();

		float min = 255;
		float max = 0;

		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {

				float pixel = ((image.getRGB(j, i) >> 16) & 0xff);

				if (pixel < min) {
					min = pixel;
				}
				if (pixel > max) {
					max = pixel;
				}
			}
		}

		System.out.println("min =" + min + "; max=" + max);
		BufferedImage img = new BufferedImage(cols, rows, 10);

		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				int pixel = ((image.getRGB(j, i) >> 16) & 0xff);
				pixel = (int) (255 * (pixel - min) / (max - min));
				int[] new_color = { pixel, pixel, pixel };
				img.getRaster().setPixel(j, i, new_color);
			}
		}

		return img;

	}

	/**
	 * Binarise l'image avec le seuil passé en paramètre
	 * @param img : l'image à binariser
	 * @param s : le seuil pour binariser
	 * @return l'image binarisée
	 */
	public static BufferedImage ImgBinairisation(BufferedImage img, int s) {
		int rows = img.getHeight();
		int cols = img.getWidth();

		BufferedImage img_binary = new BufferedImage(cols, rows, BufferedImage.TYPE_INT_RGB);

		int[] noir = { 0, 0, 0, 255 };
		int[] blanc = { 255, 255, 255, 255 };
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				int pixel = img.getRGB(j, i);
				int red = (pixel >> 16) & 0xff;
				if (red < s) {
					img_binary.getRaster().setPixel(j, i, noir);
				} else {
					img_binary.getRaster().setPixel(j, i, blanc);
				}
			}
		}
		return img_binary;
	}

	/**
	 * Appliquer un filtre médian sur l'image passée en paramètre
	 * @param img : l'image à laquelle on veut appliquer un filtre médian
	 * @return l'image à laquelle on a appliquer un filtre médian
	 */
	public static BufferedImage filtreMedian(BufferedImage img) {
		Color[] pixel = new Color[9];
		int[] R = new int[9];
		int[] B = new int[9];
		int[] G = new int[9];

		for (int i = 1; i < img.getWidth() - 1; i++)
			for (int j = 1; j < img.getHeight() - 1; j++) {
				pixel[0] = new Color(img.getRGB(i - 1, j - 1));
				pixel[1] = new Color(img.getRGB(i - 1, j));
				pixel[2] = new Color(img.getRGB(i - 1, j + 1));
				pixel[3] = new Color(img.getRGB(i, j + 1));
				pixel[4] = new Color(img.getRGB(i + 1, j + 1));
				pixel[5] = new Color(img.getRGB(i + 1, j));
				pixel[6] = new Color(img.getRGB(i + 1, j - 1));
				pixel[7] = new Color(img.getRGB(i, j - 1));
				pixel[8] = new Color(img.getRGB(i, j));
				for (int k = 0; k < 9; k++) {
					R[k] = pixel[k].getRed();
					B[k] = pixel[k].getBlue();
					G[k] = pixel[k].getGreen();
				}
				Arrays.sort(R);
				Arrays.sort(G);
				Arrays.sort(B);
				img.setRGB(i, j, new Color(R[4], B[4], G[4]).getRGB());
			}
		return img;
	}

	/**
	 * Permet de sélectionner juste les pixels sur la colonne centrale de l'image
	 * @param img : l'image qu'on souhaite traitée
	 * @return une image avec juste la colonne de pixels du milieu de l'image
	 */
	public static BufferedImage cropImage(BufferedImage img) {
		int height = img.getHeight();
		int width = img.getWidth();
		int new_height = height;
		int new_width = (int) width / 2;
		BufferedImage dest = img.getSubimage(new_width, 0, 1, new_height);
		return dest;
	}
	
	/**
	 * Calcule l'histogramme normalisé d'une image
	 * @param img : l'image dont on veut calculer l'histogramme
	 * @return l'histogramme normalisé de l'image
	 */
	public static BufferedImage CalculHist(BufferedImage img) {
		int[] hist = new int[256];
		for (int i = 0; i < img.getWidth(); i++) {
			for (int j = 0; j < img.getHeight(); j++) {

				int p = img.getRGB(i, j);

				int b = p & 0xff;
				hist[b]++;

			}
		}
		int m = hist[0];
		for (int i = 0; i < 256; i++) {
			if (hist[i] > m) {
				m = hist[i];
			}
		}
		int[] hist_norm = new int[256];
		for (int i = 0; i < 256; i++) {
			hist_norm[i] = (hist[i] * 100 / m);

		}
		BufferedImage histo_norm = new BufferedImage(256, 100, BufferedImage.TYPE_4BYTE_ABGR);
		for (int i = 0; i < 256; i++) {
			for (int j = 0; j < 100; j++) {
				histo_norm.setRGB(i, j, Color.BLACK.getRGB());
			}
		}
		for (int i = 0; i < 256; i++) {
			for (int j = 0; j < 100; j++) {
				if (j < hist_norm[i])
					histo_norm.setRGB(i, 99 - j, Color.RED.getRGB());
			}
		}
		return histo_norm;
	}

	static int[] iarray = new int[1];


	/**
	 * Calcule l'histogramme de projection de l'image
	 * @param img_binary : une image binarisée
	 * @return le tracé de l'histogramme de projection
	 */
	public static BufferedImage HistoProjection(BufferedImage img_binary) {
		int height = img_binary.getHeight();
		int width = img_binary.getWidth();
		int[] tab = new int[height];
		BufferedImage histo_projection = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				if (img_binary.getRGB(i, j) == Color.WHITE.getRGB()) {
					tab[i]++;
				}
			}
		}
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				histo_projection.setRGB(i, j, Color.BLACK.getRGB());
				if (j < tab[i]) {
					histo_projection.setRGB(i, j, Color.WHITE.getRGB());
				}
			}
		}
		return histo_projection;
	}

	/**
	 * Compte les changements de couleur d'une image
	 * @param img : une image dont on souhaite compter les changements de couleur
	 * @return le nombre de changement de couleur
	 */
	public static int CompterMarches(BufferedImage img) {
		int sum = 0, nb = 0;
		for (int i = 0; i < img.getWidth(); i++) {
			for (int j = 0; j < img.getHeight(); j++) {
				if (j > 1 && img.getRGB(i, j) != img.getRGB(i, j - 1)) {
					sum++;
				}
			}
		}
		nb = sum/2 + sum%2;
		return nb;
	}
}
