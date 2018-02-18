package model;

import android.content.Context;

import java.util.ArrayList;

/**
 * This class handles the account objects
 */
public class AccountControl {

    private ArrayList<Account> accountList;
    private FileHandler fileHandler;

    /**
     * Constructor
     * @param context the context to be passed to the FileHandler object
     */
    public AccountControl(Context context){
        fileHandler=new FileHandler(context,"data");
        fileHandler.decrypt("Any16digitworks!"); //a better key should probably used, also stored on a server maybe?
        String data[]=fileHandler.getData();
        accountList=new ArrayList<>();
        for(int i=0;i<data.length/3;i++){
            accountList.add(new Account(data[3*i],data[3*i+1],data[3*i+2]));
        }
    }

    /**
     * Transforms the list of accounts into a list of Strings and writes it in the
     * decrypted file, then creates the encrypted file and deletes the decrypted copy,
     * should ALWAYS be called when closing the application
     */
    public void updateFile(){
        ArrayList<String> stringList=new ArrayList<>();
        for(Account a:accountList){
            stringList.add(a.getDomain());
            stringList.add(a.getUsername());
            stringList.add(a.getPassword());
        }
        String[] stringArray=new String[stringList.size()];
        stringArray=stringList.toArray(stringArray);
        fileHandler.writeData(stringArray);
        fileHandler.encrypt("Any16digitworks!"); //same thing
        fileHandler.coverTracks();
    }

    /**
     * Modify the account at a given position in the list by appending a new object and removing the old one
     * @param position the position where the modification takes place
     * @param domain the new domain
     * @param username the new username
     * @param password the new password
     */
    public void modifyAccount(int position, String domain, String username, String password){
        accountList.add(position, new Account(domain, username, password));
        accountList.remove(position+1);
    }

    /**
     * Add a new account to the list
     * @param domain the new account's domain
     * @param username the new account's username
     * @param password the new account's password
     */
    public void addAccount(String domain, String username, String password){
        accountList.add(new Account(domain, username, password));
    }

    /**
     * Delete an account from the list from a given position
     * @param position the position
     */
    public void deleteAccount(int position){
        accountList.remove(position);
    }

    /**
     * Get a list of the domains of all the accounts
     * @return the list of domains
     */
    public String[] getAccountDomains(){
        String data[]=new String[accountList.size()];
        for(int i=0;i<accountList.size();i++){
            data[i]=accountList.get(i).getDomain();
        }
        return data;
    }

    /**
     * Get the account at a specific position
     * @param position the position
     * @return the account
     */
    public String[] getAccount(int position){
        String[] accountData=new String[3];
        Account account=accountList.get(position);
        accountData[0]=account.getDomain();
        accountData[1]=account.getUsername();
        accountData[2]=account.getPassword();
        return accountData;
    }
}
