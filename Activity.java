public class Activity {
    private String bookId;
    private String borrowerName;
    private String startTime;
    private String endTime;

    // Constructor
    public Activity(String bookId, String borrowerName, String startTime, String endTime) {
        this.bookId = bookId;
        this.borrowerName = borrowerName;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    // Getters and Setters
    public String getBookId() {
        return bookId;
    }

    public String getBorrowerName() {
        return borrowerName;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return "Book ID: " + bookId + ", Borrower: " + borrowerName + ", Start Time: " + startTime + ", End Time: " + endTime;

    }
}
