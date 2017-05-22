package com.ckcest.hierarchicalCluster;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class HCTest {
	public static final String sentDocsavepath = "file/catalogdata_doc2vec_for_HCC1.2";
	public static final String sentWordsavepath = "file/catalogdata_word2vec_for_HCC1.2";
	public static final String clusterFilePath1 = "file/result_HCC1.2_1";
	public static final String clusterFilePath2 = "file/result_HCC1.2_2";
	
	public static void main(String[] args) throws IOException{
		
		
//		String inputPath = "file//HCC_test.txt";
//		String outputPath = "file//HCC_test_result";
		double disThreshold = 0.8;
		
		//加载数据
		ArrayList<String> sentencelist = new ArrayList<>();
		ArrayList<Integer> senindex = new ArrayList<>();
		int count = 0;
		try (InputStreamReader output1 = new InputStreamReader(new FileInputStream(sentWordsavepath), "utf-8")) {
			BufferedReader reader = new BufferedReader(output1);
			String line = "";
			while((line = reader.readLine()) != null) {
//				if (count == 10) break;
				sentencelist.add(line.split("\t")[0].split(":")[1]);
				senindex.add(count);
				count++;
			}
		}
		ArrayList<double[]> sen2vec_word = TextSimilarity.getVector(sentWordsavepath);
		ArrayList<double[]> sen2vec_doc = TextSimilarity.getVector(sentDocsavepath);
		//计算相似度矩阵
		double[][] simmatrix1 = TextSimilarity.CalSimMatrix(sen2vec_word);
		double[][] simmatrix2 = TextSimilarity.CalSimMatrix(sen2vec_doc);
		//层次聚类
		HierarchicalClustering hc = new HierarchicalClustering();
		List<Cluster> cluste_res1 = hc.starAnalysis(senindex, simmatrix1, disThreshold);
		List<Cluster> cluste_res2 = hc.starAnalysis(senindex, simmatrix2, disThreshold);
		HierarchicalClustering.writeClusterToFile(clusterFilePath1, cluste_res1, sentencelist);
		HierarchicalClustering.writeClusterToFile(clusterFilePath2, cluste_res2, sentencelist);
	}
}

