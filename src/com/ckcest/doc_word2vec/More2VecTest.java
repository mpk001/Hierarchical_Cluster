package com.ckcest.doc_word2vec;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.ansj.vec.Learn;
import com.ansj.vec.LearnMoreVec;
import com.ansj.vec.domain.Neuron;
import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.seg.common.Term;

public class More2VecTest {
	
//	public static final String trainPath = "file/catalog_lever1_seg1.2";
//	public static final String contextDir = "doc_word2vec_model";
	public static final String trainPath = "file/catalog_alllever_seg_v2";
	public static final String contextDir = "doc_word2vec_model_v2";
	public static final String configPath = contextDir + "/doc2vec.property";
	public static ArrayList<String> Doc = new ArrayList<>();
	public static final String sentpath = "file/catalogdata_for_HCC1.2";
	public static final String sentDocsavepath = "file/catalogdata_doc2vec_for_HCC1.2";
	public static final String sentWordsavepath = "file/catalogdata_word2vec_for_HCC1.2";
	
	
	public static void main(String[] args) throws IOException, ClassNotFoundException {
//		train(); //训练模型，假如已经有上下文和配置文件了就不需要再训练
		getsen2vec();
	} 
	
	public static void train() throws IOException{
		File result = new File(trainPath);
		Learn learn = new Learn();
		learn.learnFile(result);
		Map<String, Neuron> word2vec_model = learn.getWord2VecModel();
		LearnMoreVec learn_doc = new LearnMoreVec(word2vec_model);
		learn_doc.learnFile(result, new File(configPath));
		learn_doc.saveContext(contextDir);
	}
	
	public static void getsen2vec() throws IOException, ClassNotFoundException {
		LearnMoreVec learn_doc = new LearnMoreVec(null);
		learn_doc.loadProperty(new File(configPath));
		learn_doc.loadContext(contextDir);
		try (InputStreamReader output1 = new InputStreamReader(new FileInputStream(sentpath), "utf-8")) {
			BufferedReader reader = new BufferedReader(output1);
			
			FileOutputStream output = new FileOutputStream(sentWordsavepath);
			OutputStreamWriter writer = new OutputStreamWriter(output, "utf-8");
			StringBuilder sent = new StringBuilder(); 
			
			FileOutputStream output3 = new FileOutputStream(sentDocsavepath);
			OutputStreamWriter writer3 = new OutputStreamWriter(output3, "utf-8");
			StringBuilder sent3 = new StringBuilder(); 
			
			int count0 = 0;
			String line = "";
			int c = 0;
			while((line = reader.readLine()) != null) {
				c++;
				String catalog = line.split(":")[1];
//				if (Integer.valueOf(docid) == 775203)
//					System.out.println(docid);
				String docid = line.split(":")[0];
				
				List<Term> termlist = HanLP.segment(catalog);
				String word = "";
				for (int j = 0; j < termlist.size(); j++) {					
					Term t = termlist.get(j);
					word += t.word + " ";
				}
				String[] words = word.trim().split(" ");
				double[] vector = new double[200];
				int count = 0;
				for (int i = 0; i < words.length; i++) {
					double[] vector0 = learn_doc.getw2vec(words[i]);
					if (vector0 != null) {
						for (int j = 0; j < vector.length ; j++) {
							vector[j] += vector0[j];							
						}
						count++;	
					}
					else {
						System.out.println(words[i]);
					}
				}	
				
				if (count > 0) {
					
					sent3.append(line.trim() + "\t");
					int i0 = 0;
					float[] vec = learn_doc.getd2vec(Integer.valueOf(docid));
					for (; i0 < vec.length - 1; i0++) {
						sent3.append(vec[i0] + " ");
					}
					sent3.append(vec[i0]);
					sent3.append("\r\n");
					
					sent.append(line.trim() + "\t");
					for (int j = 0; j < vector.length ; j++) {
						vector[j] /= count;
					}
					int i = 0;
					for (; i < vector.length - 1; i++) {
						sent.append(vector[i] + " ");
					}					
					sent.append(vector[i]);
					
					sent.append("\r\n");
				}	
				else {
					count0++;
					System.out.println(line + "\t" + word);
				}
			}
			reader.close(); 
			
			System.out.println(count0);
			writer.write(sent.toString());
			writer.flush();
			writer.close();
			
			writer3.write(sent3.toString());
			writer3.flush();
			writer3.close();
		}
	}
}
