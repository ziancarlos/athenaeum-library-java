package model;

public class BookkeepingBorrowing extends Bookkeeping {
    private int borrowingId;

    public BookkeepingBorrowing(int id, String doubleEntryType, String transactionType, double amount,
            String paymentDate, int borrowingId) {
        super(id, doubleEntryType, transactionType, amount, paymentDate);
        // TODO Auto-generated constructor stub
        this.borrowingId = borrowingId;
    }

    public void setBorrowingId(int id) {
        this.borrowingId = id;
    }

    public int getBorrowingId() {
        return borrowingId;
    }

}
