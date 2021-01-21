package projetinit;

public class OtsuThresholding {
	private int histData[];
	private int maxValue;
	private int threshold;

	public OtsuThresholding() {
		histData = new int[256];
	}

	/**
	 * Cherche le seuil grâce à la méthode Otsu
	 * @param srcData : les données de l'image dont ont cherche le seuil grâce à la méthode Otsu
	 * @return le seuil
	 */
	public int doThreshold(byte[] srcData) {
		int ptr;

		// Clear histogram data
		// Set all values to zero
		ptr = 0;
		while (ptr < histData.length)
			histData[ptr++] = 0;

		// Calculate histogram and find the level with the max value
		// Note: the max level value isn't required by the Otsu method
		ptr = 0;
		maxValue = 0;
		while (ptr < srcData.length) {
			int h = 0xFF & srcData[ptr];
			histData[h]++;
			if (histData[h] > maxValue)
				maxValue = histData[h];
			ptr++;
		}

		// Total number of pixels
		int total = srcData.length;

		float sum = 0;
		for (int t = 0; t < 256; t++)
			sum += t * histData[t];

		float sumB = 0;
		int wB = 0;
		int wF = 0;

		float varMax = 0;
		threshold = 0;

		for (int t = 0; t < 256; t++) {
			wB += histData[t]; // Weight Background
			if (wB == 0)
				continue;

			wF = total - wB; // Weight Foreground
			if (wF == 0)
				break;

			sumB += (float) (t * histData[t]);

			float mB = sumB / wB; // Mean Background
			float mF = (sum - sumB) / wF; // Mean Foreground

			// Calculate Between Class Variance
			float varBetween = (float) wB * (float) wF * (mB - mF) * (mB - mF);

			// Check if new maximum found
			if (varBetween > varMax) {
				varMax = varBetween;
				threshold = t;
			}
		}
		

		return threshold;
	}
}
