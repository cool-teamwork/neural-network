package tianye.base.bpnn;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.google.gson.Gson;

public class DigitalClassifier {
	
	private final static String sourceBasePath="D:/some_test/data/";
	private final static int  inputLength = 28*28;
	private final static Gson gson = new Gson();
	private final static int training_thread = 10;
	public static void main(String[] args) {
		final BP bp = new BP(inputLength,15, 10,0.15,0.2);
		
		double[] trainData = new double[inputLength];
		double[] target = new double[10];
		
		long start = System.currentTimeMillis();
		final double[][][] imgs = loadImg();
		
		
		
		for (int n = 0; n < 200; n++) {
			for (int j = 0; j < 500; j++) {
				for (int i = 0; i < 10; i++) {
					trainData = imgs[i][j];
					targetv(target, i);
					bp.train(trainData, target);
				}
			}
		}
		long end = System.currentTimeMillis();
		System.out.println("training cost: "+(end-start)/1000+" s");
		
		
	/*	for (int k = 0; k < 100; k++) {
			for (int j = 0; j < 400; j++) {
				for (int i = 0; i <10; i++) {
					StringBuffer path = new StringBuffer(sourceBasePath);
					path.append(i).append("/").append(i).append("_").append(j+1).append(".bmp");
					ImgUtils.img2Vector(trainData, path.toString());
					targetv(target, i);
					bp.train(trainData, target);
//					System.out.println(i+" target: "+Arrays.toString(target));
				}
			}
		}*/
		
//		test
		double[] testData = new double[inputLength];
		float trueCnt = 0;
		float falseCnt = 0;
		for (int i = 0; i < 10; i++) {
			for (int j = 500; j >490; j--) {
				StringBuffer path = new StringBuffer(sourceBasePath);
				path.append(i).append("/").append(i).append("_").append(j).append(".bmp");
				ImgUtils.img2Vector(testData, path.toString());
				double[] res = bp.test(testData);
				double max = -1;
				Integer maxIndex = -1;
				for (int k = 0; k < res.length; k++) {
					if(res[k] > max){
						max = res[k];
						maxIndex = k;
					}
				}
				System.out.println(i+":"+maxIndex+": "+Arrays.toString(res));
				if(maxIndex == i){
					trueCnt++;
				}else{
					falseCnt++;
				}
			}
		}
		System.out.println("accuracy: "+trueCnt/(trueCnt+falseCnt));
		
		Map<String, double[][]> weight = bp.getWeight();
		for (Entry<String, double[][]> entry : weight.entrySet()) {
			String json = gson.toJson(entry.getValue());
			write(entry.getKey()+"\t"+json+System.getProperty("line.separator"));
//			double[][] fromJson = gson.fromJson(json, double[][].class);
		}
	}
	
	private static double[][][] loadImg() {
		double[][][] images = new double[10][500][];
		double[] trainData = new double[inputLength];
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 500; j++) {
				StringBuffer path = new StringBuffer(sourceBasePath);
				path.append(i).append("/").append(i).append("_").append(j+1).append(".bmp");
				ImgUtils.img2Vector(trainData, path.toString());
				images[i][j] = trainData.clone();
			}
		}
		return images;
	}

	private static void write(String content) {
		File file = new File("D:/some_test/weight.txt");
		FileWriter fw = null;
		try {
			fw = new FileWriter(file, true);
			fw.write(content);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (fw != null) {
				try {
					fw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				fw = null;
			}
		}
	}

	private static void targetv(double[] target, int val) {
		for (int i = 0; i < target.length; i++) {
			target[i]=0;
			if(i==val){
				target[i]=1;
			}
		}
	}
}
