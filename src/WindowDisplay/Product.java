package WindowDisplay;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.Objects;

import NanostockException.NotConnectedException;

/**
 * @author Emilie Laurent
 * Represent a common product
 */
public class Product extends JPanel {
    private JPanel productsLargePanel;

    private JLabel name;
    private JLabel category;
    private JLabel lotNumber;
    private JLabel expirationDate;
    private JLabel casNumber;
    private JPanel productRootPanel;

    private JButton moreProductsButton;
    private JButton trashProductsButton;
    private JButton updateQuantity;

    private JSpinner quantitySpinner;

    private JEditorPane productsDescription;

    private JLabel supplierProduct;

    private int numberId;

    private String location;

    /**
     * Construct a default product
     */
    public Product(){
        this.name.setText("");
        this.lotNumber.setText("");
        this.expirationDate.setText("");
        this.casNumber.setText("");
        this.category.setText("");
        this.productsDescription.setText("");
        this.quantitySpinner.setValue(0);
        this.supplierProduct.setText("");
        this.numberId = -1;
        this.location = "";
    }

    /**
     * Construct a product with the given information
     * @param frame: Main frame of the software
     * @param pnum: Identification number of the product in the database
     * @param lot: Lot number of the product
     * @param expiration: Expiration date of the product
     * @param quantity: Number of that product in stock
     * @param name: Name of the product
     * @param description: Description of the product
     * @param cas: CAS number of the product
     * @param category: Category of the product
     * @param user: The active user to know his privileges
     * @param supplier: All information about the firm who sold the product
     * @param sm: Stock Management page of the software
     * @param location Name of the location where the product is stocked
     */
    public Product(JFrame frame, int pnum, String lot, String expiration, int quantity, String name,
                   String description, String cas, String category, User user, String supplier, StockMangement sm, String location){

        JPanel rootProduct = productRootPanel;
        JPanel panelLarge = productsLargePanel;
        JButton moreButton = moreProductsButton;

        //Give productRootPanel values
        this.name.setText(name);
        this.lotNumber.setText(lot);
        this.expirationDate.setText(expiration);
        this.casNumber.setText(cas);
        this.category.setText(category);
        this.productsDescription.setText(description);
        this.quantitySpinner.setValue(quantity);
        this.supplierProduct.setText(supplier);
        this.numberId = pnum;
        this.location = location;

        if (Objects.equals(user.getUserPrivileges(), "Admin")){
            trashProductsButton.setVisible(true);
        } else {
            trashProductsButton.setVisible(false);
        }

        setBackground(Color.WHITE);
        setLayout(new GridLayout(0, 1));
        add(rootProduct);

        //Add listeners to buttons
        trashProductsButton.addActionListener(new ActionToDo("deleteProduct", frame, null, null, user, null, null, sm, this, null));

        moreProductsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                StockMangement sm = null;
                try {
                    sm = new StockMangement(frame, user);
                } catch (NotConnectedException | SQLException e1) {
                    e1.printStackTrace();
                }
                if(!panelLarge.isVisible()){
                    assert sm != null;
                    moreButton.setIcon(sm.getLessButtonLogo());
                    panelLarge.setVisible(true);
                } else {
                    assert sm != null;
                    moreButton.setIcon(sm.getMoreButtonLogo());
                    panelLarge.setVisible(false);
                }
            }
        });

        updateQuantity.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int quantity = (int) quantitySpinner.getValue();
                try {
                    DataBase.UpdateData.updateProductQuantity(user, quantity, getProductId());

                    JOptionPane.showMessageDialog(null, "Quantity of " + getProductName() +
                            " has been updated in database.", "Product quantity updated", JOptionPane.INFORMATION_MESSAGE);

                } catch (SQLException e1) {
                    System.out.println("error updating productRootPanel quantity " + name);
                }
            }
        });
    }

    //Getter and setter methods
    public int getProductId(){
        return this.numberId;
    }

    public String getProductName(){
        return this.name.getText();
    }

    public int getProductQuantity(){
        return (int) this.quantitySpinner.getValue();
    }

    public String getProductCategory(){
        return this.category.getText();
    }

    public String getProductLocation(){
        return this.location;
    }

    public String getLotNumber() {
        return lotNumber.getText();
    }

    public String getExpirationDate() {
        return expirationDate.getText();
    }

    public String getCasNumber() {
        return casNumber.getText();
    }

    public String getProductDescription(){
        return productsDescription.getText();
    }

    public String getProductSupplier(){
        return this.supplierProduct.getText();
    }

    public JPanel getProductRootPanel() {
        return productRootPanel;
    }

    public JPanel getProductsPanel() {
        return productRootPanel;
    }

    public String getProductCAS() { return casNumber.getText(); }

    public void setProductId(int id){
        numberId = id;
    }

    public void setProductName(String name) { this.name.setText(name); }

    public void setProductQuantity(int quantity) { this.quantitySpinner.setValue(quantity); }

    public void setProductLot(String lotNumber) { this.lotNumber.setText(lotNumber); }

    public void setProductCAS(String casNumber) { this.casNumber.setText(casNumber); }

    public void setProductExpirationDate(String date) { this.expirationDate.setText(date); }

    public void setProductCategory(String category) { this.category.setText(category); }

    public void setProductLocation(String location) { this.location = location; }

    public void setProductDescription(String description) { this.productsDescription.setText(description); }

    public void setProductSupplier(String supplier) { this.supplierProduct.setText(supplier); }

    //Custom create for some graphical components
    private void createUIComponents() {
        //Custom create
        SpinnerNumberModel spinnerModel = new SpinnerNumberModel(0,0,99999,1);
        quantitySpinner = new JSpinner(spinnerModel);
    }
}
