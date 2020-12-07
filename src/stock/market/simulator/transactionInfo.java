package stock.market.simulator/*history*/;
public class transactionInfo {
    private String stockName;
    private double price;
    private int numberOfShares;
    private boolean isBuy;
    private double balance;
    
    public transactionInfo(String sName,double sPrice, int numShares, boolean sIsBuy, double sBalance) {
        stockName = sName;
        price = sPrice;
        numberOfShares = numShares;
        isBuy = sIsBuy;
        balance = sBalance;
    }
    
    public String getStockName(){
        return stockName;
    }
    
    public double getPrice() {
        return price;
    }
    
    public int getNumberOfShares(){
        return numberOfShares;
    }
    
    public boolean getIsBuy() {
        return isBuy;
    }
    
    public double Balance() {
        return balance;
    }
    
    
    
}
