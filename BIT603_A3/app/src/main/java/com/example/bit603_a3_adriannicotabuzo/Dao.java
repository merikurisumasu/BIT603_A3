package com.example.bit603_a3_adriannicotabuzo;

import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@androidx.room.Dao
public interface Dao {

    @Insert
    public void addUser(Users user);

    @Query("SELECT * FROM Users")
    List<Users> getUsers();

    @Query("SELECT Username FROM Users")
    List<String> getUsernames();

    @Query("DELETE FROM Users WHERE Lower(Username) = :user")
    public void deleteUser(String user);

    @Query("SELECT COUNT() FROM Users WHERE Lower(Username) = :user")
    public int userInDatabase(String user);

    @Query("SELECT COUNT() FROM Users WHERE EmployeeID = :EID")
    public  int EIDINDatabase(int EID);

    @Query("SELECT COUNT() FROM Users WHERE Lower(Username) = :user")
    public int removeUserCheck(String user);

    @Query("SELECT * FROM Users WHERE Username = :user")
    public  Users getUserDetails(String user);


    @Insert
    public void addItem(Inventory inventory);

    @Query("SELECT * FROM Inventory")
    List<Inventory> getInventory();

    @Query("SELECT Name FROM Inventory")
    List<String> getInventoryItems();

    @Query("SELECT * FROM Inventory WHERE Name = :item")
    public  Inventory getInventoryItemDetails(String item);

    @Query("DELETE FROM Inventory")
    public void clearInventory();

    @Query("SELECT COUNT() FROM Inventory WHERE Lower(Name) = :itemName")
    public int itemInDatabase(String itemName);
}
