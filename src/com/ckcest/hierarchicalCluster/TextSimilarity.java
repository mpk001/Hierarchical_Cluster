package com.ckcest.hierarchicalCluster;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import com.ansj.vec.LearnMoreVec;
import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.seg.common.Term;

//import com.cadal.focusOrder.basic.ListString;
//import com.cadal.focusOrder.dataProcess.*;
public class TextSimilarity {
	
	public static ArrayList<double[]> getVector(String file) throws UnsupportedEncodingException, FileNotFoundException, IOException {
		ArrayList<double[]> vector = new ArrayList<>();
		try (InputStreamReader output1 = new InputStreamReader(new FileInputStream(file), "utf-8")) {
			BufferedReader reader = new BufferedReader(output1);
			String line = "";
			int count = 0;
			while((line = reader.readLine()) != null) {
				double[] vector0 = new double[200];
				String vec = line.split("\t")[1];
				String[] vec0 = vec.split(" ");
				for (int i = 0; i < vec0.length; i++) {
					vector0[i] = Double.valueOf(vec0[i]);
				}
				vector.add(vector0);
				count++;
//				if (count == 10) break;
			}
		}
		return vector;
	}
	
	/**
	 * ¼ÆËãÏàËÆ¶È¾ØÕó
	 * @param 
	 * @return
	 */
	public static double[][] CalSimMatrix(ArrayList<double[]> vectorlist) {
		double[][] sim = new double[vectorlist.size()][vectorlist.size()];
		for (int i = 0; i < vectorlist.size(); i++) {
			double[] vec1 = vectorlist.get(i);
			for (int j = i + 1; j < vectorlist.size(); j++) {
				double[] vec2 = vectorlist.get(j);
				sim[i][j] = getSimilarDegree0(vec1, vec2);
				sim[j][i] = sim[i][j];
			}
		}
		
		return sim;
	}
	
	public static double getSimilarDegree0(double[] vec1, double[] vec2) {
		// 
		double vector1Modulo = 0.00;// the first vec
		double vector2Modulo = 0.00;// the second vec
		double vectorProduct = 0.00; // 
		for (int i = 0; i < vec1.length; i++) {
			vector1Modulo += vec1[i] * vec1[i];
			vector2Modulo += vec2[i] * vec2[i];

			vectorProduct += vec1[i] * vec2[i];
		}
		vector1Modulo = Math.sqrt(vector1Modulo);
		vector2Modulo = Math.sqrt(vector2Modulo);
		
		double result = vectorProduct / (vector1Modulo * vector2Modulo);
		if (result > 1) result = 1.0;

		// return the cos-value of two vec
		return result;
	}
}

