public class UndoOperation {
    private String operationType;  // Type of operation (ADD, REMOVE, BORROW)
    private Book book;  // The book involved in the operation

    public UndoOperation(String operationType, Book book) {
        this.operationType = operationType;
        this.book = book;
    }

    public String getOperationType() {
        return operationType;
    }

    public Book getBook() {
        return book;
    }
}

