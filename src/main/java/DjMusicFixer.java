package main.java;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.CannotWriteException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.KeyNotFoundException;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DjMusicFixer {
	//	Properties file name
	private static String propsFileName = "fixer.properties";
	//	Properties
	private static Properties fixerProps = new Properties();
	
	/**
	 * @param args
	 * @throws CannotReadException
	 * @throws IOException
	 * @throws TagException
	 * @throws ReadOnlyFileException
	 * @throws InvalidAudioFrameException
	 * @throws CannotWriteException
	 */
	public static void main(String[] args) throws CannotReadException, IOException, TagException, ReadOnlyFileException, InvalidAudioFrameException, CannotWriteException {
		// Save properties file name, passed as first parameter of script
		getProperties(propsFileName);
				
		// Fix all files inside folder
		File sourceFolder = new File(fixerProps.getProperty("SOURCE_FOLDER"));
		fixFilesInsideFolder(sourceFolder);
	}
	
	/**
	 * @param songTitle The song title
	 * @return The song title without undesired content
	 */
	public static String fixSongTitle(String songTitle) {
		// Initialize variable fixedSongTitle
		String fixedSongTitle = songTitle;
		
		// Remove everything between square brackets
		fixedSongTitle = fixedSongTitle.replaceAll("(\\[)(.*?)(\\])", "");
		
		// Remove spaces at the start and at the end
		fixedSongTitle = fixedSongTitle.trim();
		
		// Append '(Original Mix)' to the end of the file title, if missing
		if(isSetAsTrue("SET_MIX_TYPE") && !hasMixType(fixedSongTitle)) {
			fixedSongTitle = fixedSongTitle + " (Original Mix)";
		}
		
		return fixedSongTitle;
	}
	
	/**
	 * @param songTitle
	 * @return
	 */
	public static boolean hasMixType(String songTitle) {
		Pattern p = Pattern.compile("(\\()(.*?)(\\))");
		Matcher m = p.matcher(songTitle);
		return m.find();
	}
	
	/**
	 * @param fileName File name splitted by separator
	 * @return The artist name
	 */
	public static String[] getFileNameParts(String fileName) {
		// Split the file name by hyphen
		return fileName.split("[\\s]*[-]+[\\s]*");
	}
	
	/**
	 * @param fileName Name of the audio file
	 * @return Artist name
	 */
	public static String getSongArtist(String[] fileNameParts) {
		// Return first part of array as artist name
		return fileNameParts[0];
	}
	
	/**
	 * @param fileNameParts File name splitted by separator
	 * @return The song title
	 */
	public static String getSongTitle(String[] fileNameParts) {
		// Return second part of array as song title
		return fileNameParts[1];
	}
	
	/**
	 * @param folder Folder name
	 * @throws CannotWriteException 
	 * @throws KeyNotFoundException 
	 * @throws InvalidAudioFrameException 
	 * @throws ReadOnlyFileException 
	 * @throws TagException 
	 * @throws IOException 
	 * @throws CannotReadException 
	 */
	public static void fixFilesInsideFolder(final File folderName) throws KeyNotFoundException, CannotWriteException, CannotReadException, IOException, TagException, ReadOnlyFileException, InvalidAudioFrameException {
	    for (final File fileEntry : folderName.listFiles()) {
	        if (fileEntry.isDirectory()) {
	        	fixFilesInsideFolder(fileEntry);
	        } else {
	            fixFile(fileEntry);
	        }
	    }
	}
	
	/**
	 * @param propsFileName
	 * @return
	 * @throws IOException
	 */
	public static void getProperties(String propsFileName) throws IOException {
		//	Find properties file
		InputStream input = null;
		input = new FileInputStream(propsFileName);
		//	Load properties from properties file
		fixerProps.load(input);
	}
	
	/**
	 * @param testFile
	 * @throws KeyNotFoundException
	 * @throws CannotWriteException
	 * @throws CannotReadException
	 * @throws IOException
	 * @throws TagException
	 * @throws ReadOnlyFileException
	 * @throws InvalidAudioFrameException
	 */
	public static void fixFile(File testFile) throws KeyNotFoundException, CannotWriteException, CannotReadException, IOException, TagException, ReadOnlyFileException, InvalidAudioFrameException {
		// Read file as audio file
		AudioFile f = AudioFileIO.read(testFile);
		
		// Get mp3 tags
		Tag tag = f.getTag();
		
		// Get artist name and title
		String fileNameWithoutExtension = testFile.getName().replaceFirst("[.][^.]+$", "");
		String[] fileNameParts = getFileNameParts(fileNameWithoutExtension);
		String songArtist = getSongArtist(fileNameParts);
		String songTitle = getSongTitle(fileNameParts);
		
		// Fix the song title
		String fixedSongTitle = fixSongTitle(songTitle);
		
		// Set desired tags
		tag.setField(FieldKey.ARTIST, songArtist);
		tag.setField(FieldKey.TITLE, fixedSongTitle);
		
		// Set common tags
		tag.setField(FieldKey.ALBUM, fixerProps.getProperty("ALBUM"));
		tag.setField(FieldKey.GENRE, fixerProps.getProperty("GENRE"));
		
		// Remove undesired tags
		if(isSetAsTrue("REMOVE_COMMENT")) {
			tag.deleteField(FieldKey.COMMENT);
		}
		if(isSetAsTrue("REMOVE_COMPOSER")) {
			tag.deleteField(FieldKey.COMPOSER);
		}
		if(isSetAsTrue("REMOVE_TRACKNO")) {
			tag.deleteField(FieldKey.TRACK);
		}
		if(isSetAsTrue("REMOVE_ARTWORK")) {
			tag.deleteField(FieldKey.COVER_ART);
		}
		
		// Save file tags
		f.commit();
		
		// Rename file
		File newFileName = new File(testFile.getPath().replace(fileNameWithoutExtension, songArtist + " - " + fixedSongTitle));
		testFile.renameTo(newFileName);
	}
	
	/**
	 * @param propName
	 * @return
	 */
	public static boolean isSetAsTrue(String propName) {
		return fixerProps.getProperty(propName).equals("true");
	}
}
