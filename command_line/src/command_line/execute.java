package command_line;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import string_operations.StrOps;
import tools.Basics;
import file_operations.FileOps;

public class execute {
	private final static String zip_path = "7z";
	private final static String javac_path = "javac";

	public static void extract(String file, String destination){
		try{
			String file_path = StrOps.getDilineatedSubstring(file, "\\", 0, true);
			file_path = file.substring(0, file.length() - file_path.length());
			BufferedWriter bw = new BufferedWriter(new FileWriter("run.bat"));
			bw.write("cd " + '\"' + file_path + '\"' + "\r\n");
			bw.write(zip_path + " x \"" + file + "\" -o" + destination + "\r\n");
			bw.write("exit\r\n");
			bw.close();
			Runtime rt = Runtime.getRuntime();
			Process process = rt.exec("cmd /c start run.bat");
			BufferedReader inStreamReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String line = inStreamReader.readLine();
			while(line != null){
				System.out.println(line);
				line = inStreamReader.readLine();
			}
		} catch(IOException e){
			System.out.println("command_line.execute::cannot run command");
			System.exit(-1);
		}
	}
	
	public static boolean JavaCompile(String directory, ArrayList<String> java_files, boolean keep_log){
		try{
			BufferedWriter bw = new BufferedWriter(new FileWriter("run.bat"));
			bw.write("cd " + '\"' + directory + '\"' + "\r\n");
			bw.write(javac_path + " ");
			for(int ii = 0; ii < java_files.size(); ii++)
				bw.write(java_files.get(ii) + " ");
			bw.write(" 2> log.txt\r\n");
			bw.write("del *.class\r\n");
			bw.write("exit\r\n");
			bw.close();
			
			Runtime rt = Runtime.getRuntime();
			Process process = rt.exec("cmd /c start run.bat");
			Basics.delay(10000);
			
			BufferedReader br = new BufferedReader(new FileReader(directory + "\\log.txt"));
			String ff = "";
			String line = br.readLine();
			while(line != null){
				ff += line;
				ff += "\n";
				line = br.readLine();
			}
			br.close();
			
			ff = StrOps.trimString(ff);
			if(ff.equals("")){
				if(!keep_log){
					FileOps.deleteFile(directory + "\\log.txt");
				}
				return true;
			} else{
				return false;
			}
		} catch(IOException e){
			System.out.println("command_line.execute::cannot run command");
			System.exit(-1);
		}
		return false;
	}

	public static boolean JavaCompilePackage(String directory, boolean keep_log, boolean run, String run_command, String outputPath){
		try{
			BufferedWriter bw = new BufferedWriter(new FileWriter("run.bat"));
			bw.write("cd " + '\"' + directory + '\"' + "\r\n");
			bw.write(javac_path + " -d . *.java");
			bw.write(" 2> log.txt \r\n");
			if(run){
				bw.write("java \"" + run_command + "\" >> \"" + outputPath + "\"\r\n");
			}
			//bw.write("pause\r\n");
			bw.write("exit\r\n");
			bw.close();
			
			Runtime rt = Runtime.getRuntime();
			Process process = rt.exec("cmd /c start run.bat");
			Basics.delay(20000);
			
			BufferedReader br = new BufferedReader(new FileReader(directory + "\\log.txt"));
			String ff = "";
			String line = br.readLine();
			while(line != null){
				ff += line;
				ff += "\n";
				line = br.readLine();
			}
			br.close();
			
			ff = StrOps.trimString(ff);
			if(ff.equals("")){
				if(!keep_log){
					FileOps.deleteFile(directory + "\\log.txt");
				}
				return true;
			} else{
				return false;
			}
		} catch(IOException e){
			System.out.println("command_line.execute::cannot run command");
			System.exit(-1);
		}
		return false;
	}
}


