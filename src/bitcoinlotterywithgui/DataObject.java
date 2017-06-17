/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bitcoinlotterywithgui;

/**
 *
 * @author yippy
 */
public class DataObject {
    private String address;
    private String pageNo;
    private String balance;

    public DataObject(String address, String pageNo, String balance) {
        this.address = address;
        this.pageNo = pageNo;
        this.balance = balance;
    }
    
    

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPageNo() {
        return pageNo;
    }

    public void setPageNo(String pageNo) {
        this.pageNo = pageNo;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "Data:" + "\n" + address + "\n" + "PageNo=" + pageNo + "\n" + "Balance=" + balance ;
    }
    
    
    
}
