package edu.ohiou.mfgresearch.impmas.agents;

public class LevenshteinDistance {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println(computeLevenshteinDistance("provides vendors for requirement", "provides vendors for requirement"));
		System.out.println(computeLevenshteinDistance("provides vendors for requirement", "vendors"));
		System.out.println(computeLevenshteinDistance("provides vendors for requirement", "provides"));
		System.out.println(computeLevenshteinDistance("provides vendors for requirement", "provide"));
		System.out.println(computeLevenshteinDistance("provides vendors for requirement", "worker"));
		System.out.println(computeLevenshteinDistance("provides vendors for requirement", "Some other text"));
	}


	private static int minimum(int a, int b, int c) {
		return Math.min(Math.min(a, b), c);
	}

	public static int computeLevenshteinDistance(CharSequence str1,
			CharSequence str2) {
		int[][] distance = new int[str1.length() + 1][str2.length() + 1];

		for (int i = 0; i <= str1.length(); i++)
			distance[i][0] = i;
		for (int j = 1; j <= str2.length(); j++)
			distance[0][j] = j;

		for (int i = 1; i <= str1.length(); i++)
			for (int j = 1; j <= str2.length(); j++)
				distance[i][j] = minimum(
						distance[i - 1][j] + 1,
						distance[i][j - 1] + 1,
						distance[i - 1][j - 1]
								+ ((str1.charAt(i - 1) == str2.charAt(j - 1)) ? 0
										: 1));

		return distance[str1.length()][str2.length()];
	}
}
