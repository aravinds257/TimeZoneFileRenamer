package com.company;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

public class FileRenamer {

    public static void main(String[] args) {
        File sourceFolder = new File("From Folder");
        File destinationFolder = new File("To folder");
        File[] listOfFiles = sourceFolder.listFiles();

        if(!sourceFolder.exists()){
            System.out.println("Folder does not exist.");
        }
        if(!(sourceFolder.listFiles().length > 0)){
            System.out.println("Folder is empty");
        }

        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                String priorFileName = listOfFiles[i].getName();
                System.out.println("File " + priorFileName);
                String[] fileNameString = priorFileName.split("_");
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");

                String[] dateWithextension = fileNameString[fileNameString.length -1].replace(".","/").split("/");
                String extension = dateWithextension[1];
                String dateString = dateWithextension[0];
                String[] dateStringSplit = dateString.split("-");
                Calendar cal = Calendar.getInstance();
                cal.setTimeZone(TimeZone.getTimeZone("GMT"));
                cal.set(Integer.parseInt(dateStringSplit[0]),
                        Integer.parseInt(dateStringSplit[1])-1,
                        Integer.parseInt(dateStringSplit[2]),
                        Integer.parseInt(dateStringSplit[3]),
                        Integer.parseInt(dateStringSplit[4]),
                        Integer.parseInt(dateStringSplit[5]));

                SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
                TimeZone cst = TimeZone.getTimeZone("CST");
                sdf2.setTimeZone(cst);;
                //System.out.println("Local:: " +cstDate.getTime());
                String cstDate = sdf2.format(cal.getTime());
                System.out.println("CST:: "+ cstDate);
                //CalendarDate gmtCalenderDate = new CalendarDate( );

                String updatedFileName = getUpdatedFIleName(fileNameString, cstDate) + "." + extension;

                System.out.println(updatedFileName);

                Path src = Paths.get(sourceFolder+ "/" + priorFileName);
                Path dest = Paths.get(destinationFolder + "/" +  updatedFileName);
                try {
                    Files.move(src, dest);
                } catch (IOException e) {
                    System.out.println("not able to copy");
                }
            }
        }
    }

    private static String getUpdatedFIleName(String[] fileNameString, String cstDate) {
        fileNameString[fileNameString.length -1] = cstDate;
        StringBuilder updateFileName = new StringBuilder();
        updateFileName.append(fileNameString[0]);
        for(int j =1; j < fileNameString.length; j++){
            updateFileName.append("_").append(fileNameString[j]);
        }
        return updateFileName.toString();
    }
}
