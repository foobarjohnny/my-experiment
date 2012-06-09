import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

public class TestFile
{

	public static void main(String[] args)
	{

		try {
			FileReader fr = null;
			BufferedReader br = null;
			File f = new File("C:/filedata.txt");
			if (!f.exists()) {
				System.out.println("couldn't find file");
				return;
			}
			fr = new FileReader(f);
			br = new BufferedReader(fr);
			String temp = null;
			while ((temp = br.readLine()) != null) {
				System.out.println(temp);
			}

		}
		catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
