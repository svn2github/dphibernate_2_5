package photoBrowser.utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

import com.drew.imaging.jpeg.JpegMetadataReader;
import com.drew.imaging.jpeg.JpegProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import com.drew.metadata.exif.ExifDirectory;
import com.drew.metadata.exif.ExifReader;

import photoBrowser.beans.MediaSet;
import photoBrowser.beans.Photo;
import photoBrowser.beans.PhotoDetails;

public class DirectoryParser
{
    static final int      MAX_DEPTH  = 20;  // Max 20 levels (directory nesting)
    static final String   INDENT_STR = "   ";                 // Single indent.
    static final String[] INDENTS    = new String[MAX_DEPTH]; // Indent array.

	
	public List parsePhotoDirectory(String photoDir)
	{
		INDENTS[0] = INDENT_STR;
        for (int i = 1; i < MAX_DEPTH; i++) {
           INDENTS[i] = INDENTS[i-1] + INDENT_STR;
        }

        List resultCollection = new ArrayList();
        MediaSet mediaSet = null;
		
		Scanner in = new Scanner(photoDir);
		
		File root = new File(in.nextLine());
		if (root != null && root.isDirectory()) 
		{
			/*
			mediaSet = new MediaSet();
			mediaSet.name = root.getName();	
			resultCollection.add(mediaSet);		
			listRecursively(root, 0, null);//mediaSet);
			*/
			
			listRecursively(root, 0, null, resultCollection, photoDir);//mediaSet);
		} else {
			System.out.println("Not a directory: " + root);
		}

		return resultCollection;
	}
	
	
	private void listRecursively(File fdir, int depth, MediaSet parent, List rootCollection, String rootDir)
	{
		System.out.println(INDENTS[depth] + fdir.getName());  // Print name.

		if (fdir.isDirectory() && depth < MAX_DEPTH) {
		    for (File f : fdir.listFiles()) 
		    {  // Go over each file/subdirectory.
		    	if( f.isDirectory() && parent == null )
		    	{
		    		MediaSet newParent = new MediaSet();
		    		newParent.name = f.getName();
		    		rootCollection.add(newParent);
		    		
		    		listRecursively(f, depth+1, newParent, rootCollection, rootDir);
		    	}else{
		    		listRecursively(f, depth+1, parent, rootCollection, rootDir);
		    	}
		    }
		}
		else if( fdir.getName().toLowerCase().endsWith("jpg") 
				|| fdir.getName().toLowerCase().endsWith("jpeg")
				|| fdir.getName().toLowerCase().endsWith("png")
				|| fdir.getName().toLowerCase().endsWith("gif") )
		{
			try
			{
				
				Photo p = new Photo();
				p.parent = parent;
				p.name = fdir.getName();
				p.url = fdir.getCanonicalPath().replace("\\", "/").replaceFirst(rootDir, "/pictures");
				p.photoDetails = getPhotoDetails(fdir);
				p.photoDetails.photo = p;
				p.photoDetails.dateTaken = new Date(fdir.lastModified());
				parent.children.add(p);
			}
			catch(IOException ex)
			{
				ex.printStackTrace();
			}
		}
	}
	
	
	private PhotoDetails getPhotoDetails(File jpegFile)
	{
		PhotoDetails details = new PhotoDetails();
		
		if( jpegFile.getName().toLowerCase().endsWith("jpg") || jpegFile.getName().toLowerCase().endsWith("jpeg") )
		{
			try
			{
				Metadata metadata = JpegMetadataReader.readMetadata(jpegFile);
			
				// iterate through metadata directories 
				Iterator directories = metadata.getDirectoryIterator(); 
				while (directories.hasNext()) 
				{ 
					Directory directory = (Directory)directories.next(); 
					// iterate through tags and print to System.out  
					Iterator tags = directory.getTagIterator(); 
					while (tags.hasNext()) 
					{ 
						Tag tag = (Tag)tags.next(); 
						// use Tag.toString()  
						//System.out.println(tag.getTagName() +" --- " +tag.getDescription() +" --- " );
						
						if( directory.containsTag(ExifDirectory.TAG_DATETIME) )
							details.dateTaken = directory.getDate(ExifDirectory.TAG_DATETIME);
						if( directory.containsTag(ExifDirectory.TAG_COPYRIGHT) )
							details.copyright = directory.getString(ExifDirectory.TAG_COPYRIGHT);
						if( directory.containsTag(ExifDirectory.TAG_FNUMBER) )
							details.exifFNumber = directory.getString(ExifDirectory.TAG_FNUMBER);
						if( directory.containsTag(ExifDirectory.TAG_FOCAL_LENGTH) )
							details.exifFocalLength = directory.getString(ExifDirectory.TAG_FOCAL_LENGTH);
						if( directory.containsTag(ExifDirectory.TAG_EXIF_IMAGE_HEIGHT) )
							details.exifImageHeight = directory.getString(ExifDirectory.TAG_EXIF_IMAGE_HEIGHT);
						if( directory.containsTag(ExifDirectory.TAG_EXIF_IMAGE_WIDTH) )
							details.exifImageWidth = directory.getString(ExifDirectory.TAG_EXIF_IMAGE_WIDTH);
						if( directory.containsTag(ExifDirectory.TAG_ISO_EQUIVALENT) )
							details.exifIso = directory.getString(ExifDirectory.TAG_ISO_EQUIVALENT);
						if( directory.containsTag(ExifDirectory.TAG_MODEL) )
							details.exifModel = directory.getString(ExifDirectory.TAG_MODEL);
						if( directory.containsTag(ExifDirectory.TAG_GPS_INFO) )
							details.exifGpsInfo = directory.getString(ExifDirectory.TAG_GPS_INFO);
						if( directory.containsTag(ExifDirectory.TAG_THUMBNAIL_DATA) )	
							details.exifThumbnailData = directory.getByteArray(ExifDirectory.TAG_THUMBNAIL_DATA);
						if( directory.containsTag(ExifDirectory.TAG_THUMBNAIL_IMAGE_HEIGHT) )
							details.exifThumbnailHeight = directory.getString(ExifDirectory.TAG_THUMBNAIL_IMAGE_HEIGHT);
						if( directory.containsTag(ExifDirectory.TAG_THUMBNAIL_IMAGE_WIDTH) )
							details.exifThumbnailWidth = directory.getString(ExifDirectory.TAG_THUMBNAIL_IMAGE_WIDTH);
					
					} 
				}
			}
			catch( Exception ex) 
			{
				ex.printStackTrace();
			}
		}
		
		return details;
	}
}
 