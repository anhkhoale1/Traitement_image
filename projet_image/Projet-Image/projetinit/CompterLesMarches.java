package projetinit;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;


public class CompterLesMarches {
	//imshow()
		public static void imshow(BufferedImage image) throws IOException {
			// Instantiate JFrame
			JFrame frame = new JFrame();

			// Set Content to the JFrame
			frame.getContentPane().add(new JLabel(new ImageIcon(image)));
			frame.pack();
			frame.setVisible(true);
		}
		
	public static void main(String[] args) throws Exception {
		String file_name="";
		System.out.println("Sélectionnez le numéro de l'image d'escalier dont vous souhaitez compter les marches (entre 1 et 7) :");
		Scanner sc = new Scanner(System.in);
		int s=sc.nextInt();
		sc.close();
		
		switch(s) {
		case(1) : file_name = "C:\\Users\\champ\\Documents\\Projet-Image\\Image_Projet\\esc1.jpeg"; break;
		case(2) : file_name = "C:\\Users\\champ\\Documents\\Projet-Image\\Image_Projet\\esc2.jpg"; break;
		case(3) : file_name = "C:\\Users\\champ\\Documents\\Projet-Image\\Image_Projet\\esc3.jpeg"; break;
		case(4): file_name = "C:\\Users\\champ\\Documents\\Projet-Image\\Image_Projet\\esc4.jpeg"; break;
		case(5): file_name = "C:\\Users\\champ\\Documents\\Projet-Image\\Image_Projet\\esc5.jpeg"; break;
		case(6): file_name = "C:\\Users\\champ\\Documents\\Projet-Image\\Image_Projet\\esc6.jpg"; break;
		case(7): file_name = "C:\\Users\\champ\\Documents\\Projet-Image\\Image_Projet\\esc7.jpeg"; break;
		default : System.out.println("Vous n'avez pas entré un chiffre entre 1 et 7."); System.exit(0);break;
		}
		
		File file = new File(file_name);
		BufferedImage img_source = ImgTraitement.ImgRead(file);
		
		
		imshow(img_source);
		
		// Convertit l'image en niveau de gris
		BufferedImage img_grey = MorphoMath.convertToGrayscale(img_source);
	    	
		// Recupere les données de l'image en niveau de gris
		byte[] srcData = ImgTraitement.getImgData1(file_name);
	    
		// Utilisation de la methode Otsu pour trouver le meilleur seuil
		OtsuThresholding thresholder = new OtsuThresholding();
		int threshold = thresholder.doThreshold(srcData);
	    
		// Binarisation de l'image en niveau de gris gâce au seuil
		BufferedImage img_binary = ImgTraitement.ImgBinairisation(img_grey, threshold);
		
		// Application d'un filtre médian sur l'image binarisée
		BufferedImage img_median = ImgTraitement.filtreMedian(ImgTraitement.filtreMedian(img_binary));
	    
	    // Application d'une ouverture (érosion puis dilatation)
	    BufferedImage img_open = MorphoMath.open(img_median, 2);
	    
	    //Compte le nombre de marches    
	    BufferedImage img_compter = ImgTraitement.cropImage(img_open);
	    int nb_marche = ImgTraitement.CompterMarches(img_compter);
	    
	    
		switch(s) {
		case(1) : System.out.print("Nombre de marches reel: 7, Nombre de marches detecte : " + nb_marche); break;
		case(2) : System.out.print("Nombre de marches reel: 6, Nombre de marches detecte : " + nb_marche); break;
		case(3) : System.out.print("Nombre de marches reel: 10, Nombre de marches detecte : " + nb_marche); break;
		case(4) : System.out.print("Nombre de marches reel: 6, Nombre de marches detecte : " + nb_marche); break;
		case(5) : System.out.print("Nombre de marches reel: 9, Nombre de marches detecte : " + nb_marche); break;
		case(6) : System.out.print("Nombre de marches reel: 14, Nombre de marches detecte : " + nb_marche); break;
		case(7) : System.out.print("Nombre de marches reel: 7, Nombre de marches detecte : " + nb_marche); break;
		}
	    
	    
	}

}
