package de.niroyt.nnc.utils;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.LineNumberReader;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import org.bukkit.entity.Player;

import com.google.common.io.Files;

import de.niroyt.nnc.Main;

public class LogFile {

	public static void addLine(final Player player, final String text) throws IOException {
		final File LogOrdner = new File(Main.getInstance().getDataFolder() + "/log");
		if(!LogOrdner.isDirectory() || !LogOrdner.exists()) {
			LogOrdner.mkdirs();
		}
		
		final File file = new File(Main.getInstance().getDataFolder() + "/log/" + player.getName() + "-" + player.getUniqueId() +  ".log");
		if(!file.exists()) {
			file.createNewFile();
		}
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
		Timestamp time = new Timestamp(System.currentTimeMillis());
		sdf.setLenient(false);
		String t = sdf.format(time).replaceAll(" ", "_");
				
		BufferedWriter bw = null;
		FileWriter fw = null;
		
		fw = new FileWriter(file.getAbsoluteFile(), true);
		bw = new BufferedWriter(fw);

		bw.write("[" + t + "] " + text + "\n");
		
		if (bw != null) {
			bw.close();
		}
			
		if (fw != null) {
			fw.close();
		}
		
		if(countLines(file.getAbsolutePath()) >= 500) {
			final File ZipFile = new File(Main.getInstance().getDataFolder() + "/log/" + player.getName() + "-" + player.getUniqueId() + ".zip");
			
			final File NewFile = new File(Main.getInstance().getDataFolder() + "/log/" + t.replace(":", "-") + ".log");			
			NewFile.createNewFile();
			Files.move(file.getAbsoluteFile(), NewFile.getAbsoluteFile());
			
			if(!ZipFile.exists()) {
				zipFile(NewFile.getAbsolutePath(), ZipFile.getAbsolutePath());
			} else {
				try {
					addFileToArchive(NewFile.getAbsoluteFile(), ZipFile);
				} catch (Exception e) {
					e.printStackTrace();
				}				
			}
						
			while(!NewFile.delete()) {}
			
			if(!file.exists()) {
				file.createNewFile();
			}
		}
	}	
	
	private static int countLines(final String filename) throws IOException {
		final LineNumberReader reader = new LineNumberReader(new FileReader(filename));
	    int cnt = 0;
	    String lineRead = "";
	    while ((lineRead = reader.readLine()) != null) {}

	    cnt = reader.getLineNumber(); 
	    reader.close();
	    return cnt;
	}
	
	private static void zipFile(final String inFileName, final String outFileName){ 
		ZipOutputStream zos = null; 
		FileInputStream fis = null; 
		try { 
			zos = new ZipOutputStream(new FileOutputStream(outFileName)); 
			fis = new FileInputStream(inFileName); 
			zos.putNextEntry(new ZipEntry(new File(inFileName).getName())); 
			int len; 
			byte[] buffer = new byte[2048]; 
			while ((len = fis.read(buffer, 0, buffer.length)) > 0) { 
				zos.write(buffer, 0, len); 
			} 
		} catch (FileNotFoundException e) { 
			e.printStackTrace(); 
		} catch (IOException e) { 
			e.printStackTrace(); 
		} finally { 
			if(fis != null){ 
				try { 
					fis.close(); 
				} catch (IOException e) {} 
			} 
			if(zos != null){ 
				try { 
					zos.closeEntry(); 
					zos.close(); 
				} catch (IOException e) {} 
			} 
		} 
	} 
	
	private static void addFileToArchive(final File file, final File archiveFile) throws Exception { 
		final File tmpFile = new File(System.currentTimeMillis() + "-" + System.nanoTime());
 
		final ZipFile zipSrc = new ZipFile(archiveFile);
 
		final ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(tmpFile));
 
        Enumeration srcEntries = zipSrc.entries();
        while (srcEntries.hasMoreElements()) {
            ZipEntry entry = (ZipEntry) srcEntries.nextElement();
            zos.putNextEntry(entry);
 
            BufferedInputStream bis = new BufferedInputStream(zipSrc.getInputStream(entry));
 
            while (bis.available() > 0) {
                zos.write(bis.read());
            }
            zos.closeEntry();
 
            bis.close();
        }
 
        final ZipEntry newEntry = new ZipEntry(file.getName());
        zos.putNextEntry(newEntry);
 
        final BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
        while (bis.available() > 0) {
            zos.write(bis.read());
        }
        zos.closeEntry();
 
        zos.finish();
 
        zos.close();
 
        zipSrc.close();
 
        archiveFile.delete();
 
        tmpFile.renameTo(archiveFile); 
        
        bis.close();
    }
}
