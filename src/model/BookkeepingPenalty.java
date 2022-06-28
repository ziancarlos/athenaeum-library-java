package model;

public class BookkeepingPenalty extends Bookkeeping {
    private int penaltyId;

    public BookkeepingPenalty(int id, String doubleEntryType, String transactionType, double amount,
            String paymentDate, int penaltyId) {
        super(id, doubleEntryType, transactionType, amount, paymentDate);
        // TODO Auto-generated constructor stub
        this.penaltyId = penaltyId;
    }

    public void setPenaltyId(int id) {
        this.penaltyId = id;
    }

    public int getPenaltyId() {
        return this.penaltyId;
    }

}
