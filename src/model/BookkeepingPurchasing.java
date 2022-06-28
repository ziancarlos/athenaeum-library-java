package model;

public class BookkeepingPurchasing extends Bookkeeping {
    private int purchasingId;

    public BookkeepingPurchasing(int id, String doubleEntryType, String transactionType, double amount,
            String paymentDate, int purchasingId) {
        super(id, doubleEntryType, transactionType, amount, paymentDate);
        // TODO Auto-generated constructor stub
        this.purchasingId = purchasingId;
    }

    public void setPurchasingId(int id) {
        this.purchasingId = id;
    }

    public int getPurchasingId() {
        return purchasingId;
    }

}
