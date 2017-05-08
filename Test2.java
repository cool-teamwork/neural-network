package tianye.base.bpnn;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;

import com.google.gson.Gson;

public class Test2 {

	private final static Gson gson = new Gson();
	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		BP bp = new BP(32, 15, 2);

		Random random = new Random();
		List<Integer> list = new ArrayList<Integer>();
		for (int i = 0; i != 1000; i++) {
			int value = random.nextInt(100000);
			list.add(value);
		}

		for (int i = 0; i != 200; i++) {
			for (int value : list) {
				double[] real = new double[2];
				if ((value & 1) == 1)
					real[0] = 1;
				else
					real[1] = 1;
				double[] binary = new double[32];
				int index = 31;
				do {
					binary[index--] = (value & 1);
					value >>>= 1;
				} while (value != 0);

				bp.train(binary, real);
			}
		}

		Map<String, double[][]> weight = bp.getWeight();
		for (Entry<String, double[][]> entry : weight.entrySet()) {
			String json = gson.toJson(entry.getValue());
			write(entry.getKey()+"\t"+json+System.getProperty("line.separator"));
//			double[][] fromJson = gson.fromJson(json, double[][].class);
		}
		System.out.println("训练完毕，下面请输入一个任意数字，神经网络将自动判断它是正数还是复数，奇数还是偶数。");

		while (true) {
			byte[] input = new byte[10];
			System.in.read(input);
			Integer value = Integer.parseInt(new String(input).trim());
			int rawVal = value;
			double[] binary = new double[32];
			int index = 31;
			do {
				binary[index--] = (value & 1);
				value >>>= 1;
			} while (value != 0);

			double[] result = bp.test(binary);

			double max = -Integer.MIN_VALUE;
			int idx = -1;

			for (int i = 0; i != result.length; i++) {
				if (result[i] > max) {
					max = result[i];
					idx = i;
				}
			}
			System.out.println(Arrays.toString(result));
			switch (idx) {
			case 0:
				System.out.format("%d是一个正奇数\n", rawVal);
				break;
			case 1:
				System.out.format("%d是一个正偶数\n", rawVal);
				break;
			case 2:
				System.out.format("%d是一个负奇数\n", rawVal);
				break;
			case 3:
				System.out.format("%d是一个负偶数\n", rawVal);
				break;
			}
		}
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
}
