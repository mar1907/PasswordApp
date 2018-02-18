package model;

/*
https://www.developer.com/ws/android/working-with-files-and-the-file-system-in-android-apps.html
useful when creating the file

https://www.javacodegeeks.com/2012/05/secure-password-storage-donts-dos-and.html
useful for storing the original password

http://www.codejava.net/coding/file-encryption-and-decryption-simple-example
encryption and decryption

https://stackoverflow.com/questions/27962116/simplest-way-to-encrypt-a-text-file-in-java
try this in computer java
*/

import android.content.Context;
import android.util.Log;

import java.io.*;
import java.util.ArrayList;

import cryptopack.CryptoException;
import cryptopack.CryptoUtils;

/**
 * This class acts as the interface between the app and the file
 */
public class FileHandler {

    private static final String TAG="fileHandlerMessage";

    private boolean isDecrypted;
    private File encryptedFile, decryptedFile;

    /**
     * Constructor
     * @param fileName the name of the encrypted file
     * @param context the context, useful for getting the file
     */
    public FileHandler(Context context, String fileName){
        encryptedFile=new File(context.getFilesDir(),fileName);
        decryptedFile=new File(context.getFilesDir(),"temporary");
        isDecrypted=false;
    }

    /**
     * Decrypt the file into a new temporary file
     * @param key the encryption key
     */
    public void decrypt(String key){
        try {
            CryptoUtils.decrypt(key,encryptedFile,decryptedFile);
            isDecrypted=true;
        } catch (CryptoException e) {
            e.printStackTrace();
            Log.i(TAG,"exception in decryption");
            //TODO show in monitor
        }
    }

    /**
     * Create the encrypted file
     * @param key the encryption key
     */
    public void encrypt(String key){
        try {
            CryptoUtils.encrypt(key,decryptedFile,encryptedFile);
            isDecrypted=false;
        } catch (CryptoException e) {
            e.printStackTrace();
            Log.i(TAG,"exception in encryption");
            //TODO show in monitor
        }
    }

    public boolean isDecrypted(){
        return isDecrypted;
    }

    /**
     * Get the data from the file
     * @return an array of all the data from the decrypted file
     */
    public String[] getData(){
        ArrayList<String> als=new ArrayList<String>();
        //write one word on each line to make this work
        try {
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(
                            new FileInputStream(decryptedFile), "UTF8"));

            String str;

            while ((str = in.readLine()) != null) {
                als.add(str);
            }

            in.close();
        } catch (IOException e) {
            e.printStackTrace();
            Log.i(TAG,"exception in reading data");
            //TODO show in monitor
        }

        String[] returnArray=new String[als.size()];
        returnArray=als.toArray(returnArray);
        return returnArray;
    }

    /**
     * Writes the data in the file to be encrypted
     * @param data the data to be written
     */
    public void writeData(String[] data){
        try {
            FileOutputStream fop=new FileOutputStream(decryptedFile);
            for(int i=0;i<data.length;i++){
                fop.write((data[i]+"\n").getBytes());
            }
        } catch (IOException e) {
            e.printStackTrace();
            //TODO show in monitor
        }

    }

    /**
     * Delete the decrypted file, should ALWAYS be called when closing the application
     */
    public void coverTracks(){
        decryptedFile.delete();
    }
}
