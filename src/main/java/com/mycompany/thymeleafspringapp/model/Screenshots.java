package com.mycompany.thymeleafspringapp.model;
// Generated Jan 22, 2015 10:22:48 PM by Hibernate Tools 3.6.0



/**
 * Screenshots generated by hbm2java
 */
public class Screenshots  implements java.io.Serializable {


     private long screenshotId;
     private Deals deals;
     private byte[] file;
     private String fileName;
     private String filePath;

    public Screenshots() {
    }

	
    public Screenshots(long screenshotId, Deals deals) {
        this.screenshotId = screenshotId;
        this.deals = deals;
    }
    public Screenshots(long screenshotId, Deals deals, byte[] file, String fileName, String filePath) {
       this.screenshotId = screenshotId;
       this.deals = deals;
       this.file = file;
       this.fileName = fileName;
       this.filePath = filePath;
    }
   
    public long getScreenshotId() {
        return this.screenshotId;
    }
    
    public void setScreenshotId(long screenshotId) {
        this.screenshotId = screenshotId;
    }
    public Deals getDeals() {
        return this.deals;
    }
    
    public void setDeals(Deals deals) {
        this.deals = deals;
    }
    public byte[] getFile() {
        return this.file;
    }
    
    public void setFile(byte[] file) {
        this.file = file;
    }
    public String getFileName() {
        return this.fileName;
    }
    
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    public String getFilePath() {
        return this.filePath;
    }
    
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }




}


