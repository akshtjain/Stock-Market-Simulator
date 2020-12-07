package stock.market.simulator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

import javax.swing.border.Border;
import java.util.*;
import static stock.market.simulator.StockMarketSimulator.HISTORYFILEPATH;

/*
label list
*/

public class mainWindow {

    // CURRENCY STOCK TEXT FIELDS
    JTextField EURUSD;
    JTextField diffEURUSD;

    JTextField GBPUSD;
    JTextField diffGBPUSD;

    JTextField EURGBP;
    JTextField diffEURGBP;

    JTextField GBPJPY;
    JTextField diffGBPJPY;

    // COMPANY STOCK TEXT FIELDS
    JTextField facebook;
    JTextField difffacebook;

    JTextField apple;
    JTextField diffapple;

    JTextField microsoft;
    JTextField diffmicrosoft;

    JTextField bmw;
    JTextField diffbmw;

    // ECONOMY STOCK TEXT FIELDS
    JTextField uk;
    JTextField diffuk;

    JTextField usa;
    JTextField diffusa;

    JTextField aus;
    JTextField diffaus;

    JTextField jpy;
    JTextField diffjpy;

    //textFields -> priceTextFields
    //differenceTextFields -> priceDifferenceTextFields
    //priceTextFields and priceDifferenceTextFields changed from 2D ARRAYS TO 1D ARRAYS
    JTextField[] priceTextFields;
    JTextField[] priceDifferenceTextFields;
    Button[] stockNameButton;
    
    JTextArea stocksBoughtInfo;
    accountProfile accProfile;
    stockProfile[][] stocksProfiles; //atm it is really a list of list with just one list element [[list of company stocks]]
    stockProfile [] stockProfiles;
    //company stock attrs -> 
    //god i really really miss python
    ArrayList<transactionInfo> transactionRecords = new ArrayList<>();
    // Class constructor
    public mainWindow(accountProfile account, stockProfile[][] profiles) {

        accProfile = account;
        stocksProfiles = profiles;
        stockProfiles = stocksProfiles[0];
        int numStocks = stockProfiles.length;
        ButtonListener listener = new ButtonListener();
        //Existing Structure
        /*
        stock market simulator
            Stock Info
                Currency Stock
                Company Stock
                Economy Stock
            Stocks Bought
            Button Frame
        */
        JFrame frame = new JFrame("Stock Simulator");
        frame.setLayout(new FlowLayout(FlowLayout.CENTER, 50, 20));
        
        stockNameButton = new Button[numStocks];
        priceTextFields = new JTextField[numStocks];
        priceDifferenceTextFields = new JTextField[numStocks];
        
        
        JPanel stockInfo = new JPanel();
        Border stockInfoBorder = BorderFactory.createTitledBorder("STOCK INFORMATION");
        stockInfo.setBorder(stockInfoBorder);
        stockInfo.setLayout(new GridLayout(numStocks+1, 3));
        
        stockInfo.add(new JLabel(""));
        stockInfo.add(new JLabel("Buy/Sell", SwingConstants.CENTER));
        stockInfo.add(new JLabel(""));
        for(int idx = 0; idx<numStocks; ++idx){
            stockProfile stock = stockProfiles[idx];
            Button button = new Button(stock.getProfileName());
            button.addActionListener(listener);
            stockNameButton[idx] = button;
//            stockNameButton[idx].setText(stock.getProfileName());
//            stockNameButton[idx].addActionListener(listener);
            priceTextFields[idx] = new JTextField();
            priceTextFields[idx].setText(String.valueOf(stock.getBuyPrice()) + "/" + String.valueOf(stock.getSellPrice()));
            priceTextFields[idx].setEditable(false);
            priceTextFields[idx].setBackground(Color.white);
            double change = stock.getChange();
            String difference = (change > 0) ? ("+" + change) : ("" + change);
            priceDifferenceTextFields[idx] = new JTextField();
            priceDifferenceTextFields[idx].setText(
                    difference
            );
            priceDifferenceTextFields[idx].setEditable(false);
            priceDifferenceTextFields[idx].setBackground(Color.white);
            stockInfo.add(stockNameButton[idx]);
            stockInfo.add(priceTextFields[idx]);
            stockInfo.add(priceDifferenceTextFields[idx]);
        }

        JPanel stocksBought = new JPanel();
        Border stocksBoughtBorder = BorderFactory.createTitledBorder("Stocks Bought");
        stocksBought.setBorder(stocksBoughtBorder);

        stocksBoughtInfo = new JTextArea(15, 30);
        stocksBought.add(stocksBoughtInfo);

        
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(1, 2));

        mainPanel.add(stockInfo);
        mainPanel.add(stocksBought);

        mainPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 50, 20));
        stocksBoughtInfo.setEditable(false);
        stocksBoughtInfo.setBackground(Color.white);
        JPanel buttonPanel = new JPanel();
        Border graphViewBorder = BorderFactory.createEtchedBorder();
        buttonPanel.setBorder(graphViewBorder);

        Button btnShowGraph = new Button("View Graph");
        btnShowGraph.addActionListener(listener);
        
        Button btnShowTraderGraph = new Button("View Trader Graph");
        btnShowTraderGraph.addActionListener(listener);

        Button btnBuySell = new Button("Buy/Sell Stock");
        btnBuySell.addActionListener(listener);

        buttonPanel.setLayout(new GridLayout(1, 3, 5, 5));
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));
        buttonPanel.add(btnBuySell);
        buttonPanel.add(btnShowGraph);
        buttonPanel.add(btnShowTraderGraph);
        //buttonPanel.setPreferredSize(new Dimension(110, 75));

        frame.add(mainPanel);
        frame.add(buttonPanel);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200, 650);
        frame.setLocationRelativeTo(null);

        frame.setVisible(true);

    }

    // Event handler for when buttons are pressed
    public class ButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent et) {

            String btnName = et.getActionCommand();

            try {
                if (btnName.equals("Buy/Sell Stock")) {
                    buy_sellShare window = new buy_sellShare(accProfile, stocksProfiles, btnName, transactionRecords);
                }

                else if (btnName.equals("View Graph")) {
                    viewGraph graph = new viewGraph(stocksProfiles);
                }
                else if (btnName.equals("View Trader Graph")) {
                     try {

                        String fileName = "totalBalance";

                        createGraph graph = new createGraph(fileName);

                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(null, "Error: " + e, "Error", JOptionPane.WARNING_MESSAGE);
                    }
                }
                else {
                    buy_sellShare window = new buy_sellShare(accProfile, stocksProfiles, btnName, transactionRecords);
                }
                
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Error: " + e, "Error", JOptionPane.WARNING_MESSAGE);
            }

        }

    }
    
    // Method to set the values of the text boxes with the stock prices
    public void setTextBoxValues(stockProfile[][] stocksProfiles) {

        for (int i = 0; i < stocksProfiles.length; i++) {
            for (int j = 0; j < stocksProfiles[i].length; j++) {

                stockProfile stock = stocksProfiles[i][j];

                setStockPriceTextField(priceTextFields[j], stock);
                setDifferenceTextField(priceDifferenceTextFields[j], stock.getChange());
                setTextFieldColour(priceDifferenceTextFields[j], stock.getChange());

                writeRateToFile(stock.getProfileName(), stock.getBuyPrice());

            }
        }

        setStockBought();

    }
    // Method that sets the total balance in the csv file
    public void setTotalBalance(stockProfile[][] stocksProfiles,accountProfile account ){
         ArrayList<stockProfile> stocks = account.getStocks();
         double totalBalance = account.getBalance();
         for (int i = 0;  i < stocks.size() ; i++){
             stockProfile stock = stocks.get(i);
             double currentPrice = getStockPriceByName(stocksProfiles, stock.getProfileName() );
             totalBalance += currentPrice*stock.getQuantity();
         }
         String fileName = "totalBalance";
         writeRateToFile(fileName, totalBalance);
    }
    
    private double getStockPriceByName(stockProfile[][] stocksProfiles, String stockName ){
        for (int i = 0; i < stocksProfiles.length; i++) {
            for (int j = 0; j < stocksProfiles[i].length; j++) {

                stockProfile stock = stocksProfiles[i][j];
               
                if(stock.getProfileName().equals(stockName)){
                    return stock.getSellPrice();
                }
            }
        }
        return 0.0;
    }
    // Method to set the buy and sell rates for a stock
    public void setStockPriceTextField(JTextField field, stockProfile profile) {
        field.setText(
                String.valueOf(profile.getBuyPrice()) + "/" + String.valueOf(profile.getSellPrice()));

    }

    // Setting the difference between current rate and previous rates
    public void setDifferenceTextField(JTextField field, double change) {
        String difference = (change > 0) ? ("+" + change) : ("" + change);
        field.setText(difference + "%");
    }

    // Method to display the bought stocks in the relative text area
    public void setStockBought() {
        ArrayList<stockProfile> stocksBought = accProfile.getStocks();
        int numOfStock = stocksBought.size();
        String message = "STOCK NAME - BUY PRICE/SELL PRICE - Quantity\n";

        for (int i = 0; i < numOfStock; i++) {
            message = message + stocksBought.get(i).getProfileName() + " - " + stocksBought.get(i).getBuyPrice() + "/"
                    + stocksBought.get(i).getSellPrice() + " - " + stocksBought.get(i).getQuantity() + "\n";
        }
        
        message = message + "\nBALANCE: " + roundTo2DP(accProfile.getBalance());
        int numTransactions = transactionRecords.size();
        if(numTransactions>10)
            numTransactions = 10;
        message = message + "\n\n\nPast "+numTransactions+" transactions\n";
        message += "STOCK NAME\tPRICE\tTYPE\tNUM SHARES\n";
        for(int idx = transactionRecords.size()-1; idx>=0 && idx>=transactionRecords.size()-11;idx-=1) {
            transactionInfo record = transactionRecords.get(idx);
            String transactionType = "Sell";
            if(record.getIsBuy() == true)
                transactionType = "Buy";
            message+=record.getStockName()+"\t"+record.getPrice()+"\t"+transactionType+"\t"+record.getNumberOfShares()+"\n";
        }
        
        stocksBoughtInfo.setText(message);
    }

    // Method to set the font colour of the difference
    public void setTextFieldColour(JTextField field, double change) {
        if (change >= 0) {
            field.setForeground(Color.green);
        } else {
            field.setForeground(Color.red);
        }
    }

    // Method to write the rate to its relavant file
    public void writeRateToFile(String fileName, double rate) {

        try {
            String location = HISTORYFILEPATH + fileName + ".csv";

            Calendar getTime = Calendar.getInstance();
            SimpleDateFormat formatTime = new SimpleDateFormat("HH:mm:ss");
            String timeStamp = formatTime.format(getTime.getTime());

            try (BufferedWriter write = new BufferedWriter(new FileWriter(location, true))) {
                String lineToWrite = timeStamp + "," + rate;
                write.write(lineToWrite);

                write.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error: " + e);
        }

    }
    
    // Rounding to 2 decimal place
    public static Double roundTo2DP(double number) {
        DecimalFormat roundFormat = new DecimalFormat(".##");
        return (Double.parseDouble(roundFormat.format(number)));
    }

}
