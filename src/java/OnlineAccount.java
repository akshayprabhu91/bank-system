


import java.io.Serializable;
import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.text.*;
import java.util.List;
import javax.faces.context.FacesContext;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;




public class OnlineAccount {
    
    private String id;   //account ID
    private String password;   //saved password
    private ArrayList<BankAccount> managedAccounts;  //the accounts under this ID;
    private String ssNumber;
    private List<String> managedAccountsID;
    private String selectedAccountToView;
    
    private String oldPsw;
    private String newPsw1;
    private String newPsw2;
    
    private String fromAccount;
    private String toAccount;
    private double transferAmount;
    
    
    //constructor
    public OnlineAccount(String selectedID, String aSSN, String selectedPassword)
    {
        id = selectedID;
        password = selectedPassword;
        ssNumber = aSSN;
        selectedAccountToView = "";
        oldPsw = "";
        newPsw1 = "";
        newPsw2 = "";
        
        managedAccounts = new ArrayList<BankAccount> ();
        managedAccountsID = new ArrayList<String> ();
        
       
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        
        try
        {
            final String DATABASE_URL = "jdbc:mysql://mis-sql.uhcl.edu/drlin";
            
            //connect to the database with user name and password
            connection = DriverManager.getConnection(DATABASE_URL, "DrLin", "UHCL2014");   
            statement = connection.createStatement();
            
            resultSet = statement.executeQuery("Select * from bankAccount where ssn = '" + ssNumber + "'" );
            
            while(resultSet.next())
            {
                managedAccounts.add(new BankAccount(resultSet.getString(1), resultSet.getString(2), resultSet.getDouble(3), 
                        resultSet.getString(4)));
                managedAccountsID.add(resultSet.getString(1));
            }
        }
        catch (SQLException e)
        {           
            e.printStackTrace();
        }
        finally
        {
            try
            {
                resultSet.close();
                statement.close();
                connection.close();
            }
            catch (Exception e)
            {
                e.printStackTrace();         
            }
        }
    
        
    }

    public String getFromAccount() {
        return fromAccount;
    }

    public void setFromAccount(String fromAccount) {
        this.fromAccount = fromAccount;
    }

    public String getToAccount() {
        return toAccount;
    }

    public void setToAccount(String toAccount) {
        this.toAccount = toAccount;
    }

    public double getTransferAmount() {
        return transferAmount;
    }

    public void setTransferAmount(double transferAmount) {
        this.transferAmount = transferAmount;
    }

    
    
    public void setOldPsw(String oldPsw) {
        this.oldPsw = oldPsw;
    }

    public void setNewPsw1(String newPsw1) {
        this.newPsw1 = newPsw1;
    }

    public void setNewPsw2(String newPsw2) {
        this.newPsw2 = newPsw2;
    }
    
    //return the online Account ID
    public String getID()
    {
        return id;
    }
    
    //return the password saved
    public String getPassword()
    {
        return password;
    }

    public String getOldPsw() {
        return oldPsw;
    }

    public String getNewPsw1() {
        return newPsw1;
    }

    public String getNewPsw2() {
        return newPsw2;
    }
    
    public String getSSN()
    {
        return ssNumber;
    }

    public String getSelectedAccountToView() {
        return selectedAccountToView;
    }

    public ArrayList<BankAccount> getManagedAccounts() {
        return managedAccounts;
    }
    

    public List<String> getManagedAccountsID() {
        return managedAccountsID;
    } 
    
    //reset the password
    public void setPassword(String thePassword)
    {
        password = thePassword;
    }

    public void setSelectedAccountToView(String selectedAccountToView) {
        this.selectedAccountToView = selectedAccountToView;
    }

    public List<String> showSelectedStatement()
    {
        int index = -1;
        for(int i=0; i<managedAccounts.size(); i++)
        {
            if(managedAccounts.get(i).getAccountNumber().equals(selectedAccountToView))
            {
                index = i;
                break;
            }
        }
        
        if(index < 0)
        {
            return null;
        }
        else
        {
            return managedAccounts.get(index).showStatement();
        }
        
    }
    
    
    public String accountTransfer()
    {
         
        int toIndex = 0; 
        int fromIndex = 0;
        for(int i=0; i<managedAccounts.size(); i++)
        {
            if(managedAccounts.get(i).getAccountNumber().equals(toAccount))
            {
                toIndex = i;
            }
            
            if(managedAccounts.get(i).getAccountNumber().equals(fromAccount))
            {
                fromIndex = i;
            }
            
        }
            
            
            
        if(managedAccounts.size() < 2)
        {
            fromAccount = managedAccounts.get(0).getAccountNumber();
            toAccount = managedAccounts.get(0).getAccountNumber();
            transferAmount = 0.0;
             return("You must have at least two different online accounts to do account transfer");
        }
        else
        { 
             if(!fromAccount.equals(toAccount))
             {
                 if(managedAccounts.get(fromIndex).getBalance() < transferAmount)
                 {
                     fromAccount = managedAccounts.get(0).getAccountNumber();
                     toAccount = managedAccounts.get(0).getAccountNumber();
                     transferAmount = 0.0;
                     
                     return("The account used to transfer the money from has no enough fund!");
                 }
                 else
                 {
                     managedAccounts.get(toIndex).deposit(transferAmount);
                     managedAccounts.get(fromIndex).withdraw(transferAmount);
                          
                     fromAccount = managedAccounts.get(0).getAccountNumber();
                     toAccount = managedAccounts.get(0).getAccountNumber();
                     transferAmount = 0.0;
                     return("The transfer of money was successful!");
                 }
             }
             else
             {
                 fromAccount = managedAccounts.get(0).getAccountNumber();
                 toAccount = managedAccounts.get(0).getAccountNumber();
                 transferAmount = 0.0;
                 return ("You must select two different accounts");
                             
             }
        }
 
    }
    
    public String signOut()
    {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return "index.xhtml";

        
    }
    
}

