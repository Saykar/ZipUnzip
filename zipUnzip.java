import java.util.*;
import java.util.zip.*;
import java.io.*;

public class ZipUnzip {
	private static final int BUFFER_SIZE = 4096;

	/**
	 * Compresses a collection of files to a destination zip file
	 * @param directoryToZip A collection of files and directories
	 * @param destZipFile The path of the destination zip file
	 */
	public void exportZip(String srcPath, String destZipFile) {
		try{
			File directoryToZip = new File(srcPath);
			List<File> listFiles = new ArrayList<File>(1);
			listFiles.add(directoryToZip);
			ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(destZipFile));

			for (File file : listFiles){
				if (file.isDirectory()) {
					addFolderToZip(file, file.getName(), zos);
				} else {
					//to omit a few files from the destination zip directory
					if(!file.getName().equals("fileToOmit1.pdf") && !file.getName().equals("fileToOmit1.py")){
						zos.putNextEntry(new ZipEntry(file.getName()));
						addFileToZip(file, zos);
					}
				}
			}
			zos.flush();
			zos.close();
			System.out.println("Zip exported successfully");	
		}
		catch(Exception e){
			System.out.println(e.getMessage());
		}
	}

	/**
	 * Adds a directory to the current zip output stream
	 * @param folder the directory to be  added
	 * @param parentFolder the path of parent directory
	 * @param zos the current zip output stream
	 */
	private void addFolderToZip(File folder, String parentFolder, ZipOutputStream zos) {
		try{
			for (File file : folder.listFiles()) {
				if (file.isDirectory()) {
					addFolderToZip(file, parentFolder + "/" + file.getName(), zos);
					continue;
				}
				if(!file.getName().equals("fileToOmit1.pdf") && !file.getName().equals("fileToOmit1.py")){
					zos.putNextEntry(new ZipEntry(parentFolder + "/" + file.getName()));

					addFileToZip(file,zos);
				}
			}
		}
		catch(Exception e){
			System.out.println(e.getMessage());
			System.exit(0);
		}
	}

	/**
	 * Adds a file to the current zip output stream
	 * @param file the file to be added
	 * @param zos the current zip output stream
	 */
	private void addFileToZip(File file, ZipOutputStream zos){
		try{
			BufferedInputStream bis = new BufferedInputStream(new FileInputStream(
					file));

			//long bytesRead = 0;
			byte[] bytesIn = new byte[BUFFER_SIZE];
			int read = 0;

			while ((read = bis.read(bytesIn)) != -1) {
				zos.write(bytesIn, 0, read);
				// bytesRead += read;
			}

			zos.closeEntry();
		}
		catch(Exception e){
			System.out.println(e.getMessage());
			System.exit(0);
		}
	}
	/**
	 *
	 * @param zipPath Path of the zip file to be extracted
	 * @param destDirectory Path of the destination directory
	 */
	public void importZip(String zipPath, String destDirectory) {
		try{
			File destDir = new File(destDirectory);
			if (!destDir.exists()) {
				destDir.mkdir();
			}

			ZipInputStream zipIn = new ZipInputStream(new FileInputStream(zipPath));

			ZipEntry entry = zipIn.getNextEntry();

			while (entry != null) {
				String filePath = destDir + File.separator + entry.getName();
				if (!entry.isDirectory()) {
					extractFile(zipIn, filePath);
				} else {
					File dir = new File(filePath);
					dir.mkdir();
				}
				zipIn.closeEntry();
				entry = zipIn.getNextEntry();
			}
			zipIn.close();
			System.out.println("Zip imported successfully");
		}
		catch(Exception e){
			System.out.println(e.getMessage());		
		}
	}

	/**
	 * Extracts a single file
	 * @param zipIn the ZipInputStream
	 * @param filePath Path of the destination file
	 */
	private void extractFile(ZipInputStream zipIn, String destFilePath) {
		try{
			//if file exists, replace it. 
			//if file does NOT exist, copy it.

			File destFile = new File(destFilePath);
			if(!destFile.exists()){
				destFile.getParentFile().mkdirs();
			}

			BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(destFile));
			byte[] bytesIn = new byte[BUFFER_SIZE];
			int read = 0;
			while ((read = zipIn.read(bytesIn)) != -1) {
				bos.write(bytesIn, 0, read);
			}  
			bos.close();   
		}
		catch(Exception e){
			System.out.println(e.getMessage());
			System.exit(0);
		}
	}

}
