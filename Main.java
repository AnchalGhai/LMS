import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Library library = new Library(2); // Initial capacity of 10 books/activities

        // Sample menu for user interaction
        while (true) {
            System.out.println("Library Menu:");
            System.out.println("1. Add Book");
            System.out.println("2. Remove Book");
            System.out.println("3. Display All Books");
            System.out.println("4. Sort Books by Book ID");
            System.out.println("5. Borrow Book");
            System.out.println("6. Return Book");
            System.out.println("7. Display Activity Log");
            System.out.println("8. Undo Last Operation");
            System.out.println("9. Search for a book");
            System.out.println("10. Exit");
            System.out.print("Choose an option: ");
            String choice = scanner.nextLine();

            // Convert the string input to an integer manually
            int option = convertToInt(choice);

            // If conversion is unsuccessful, prompt the user again
            if (option == -1) {
                System.out.println("Invalid choice. Please enter a valid number.");
                continue;
            }

            switch (option) {
                case 1:
                    // Add Book
                    System.out.print("Enter Book ID: ");
                    String bookId = scanner.nextLine();
                    System.out.print("Enter Title: ");
                    String title = scanner.nextLine();
                    System.out.print("Enter Author: ");
                    String author = scanner.nextLine();
                    Book book = new Book(bookId, title, author);
                    library.addBook(book);
                    break;

                case 2:
                    // Remove Book
                    System.out.print("Enter Book ID to remove: ");
                    String removeBookId = scanner.nextLine();
                    library.removeBook(removeBookId);
                    break;

                case 3:
                    // Display all Books
                    library.displayAllBooks();
                    break;

                case 4:
                    // Sort Books by Book ID (Counting Sort)
                    library.countingSortBooks();
                    break;

                case 5:
                    // Borrow Book
                    System.out.print("Enter Book ID to borrow: ");
                    String borrowId = scanner.nextLine();
                    System.out.print("Enter Borrower Name: ");
                    String borrowerName = scanner.nextLine();
                    library.borrowBook(borrowId, borrowerName);
                    break;

                case 6:
                    // Return Book
                    System.out.print("Enter Book ID to return: ");
                    String returnId = scanner.nextLine();
                    library.returnBook(returnId);
                    break;

                case 7:
                    // Display Activity Log
                    library.displayActivityLog();
                    break;

                case 8:
                    // Undo Last Operation
                    library.undoLastOperation();
                    break;

                case 9:
                    // Search for books
                    System.out.print("Enter Book ID to search: ");
                    String searchBookId = scanner.nextLine();
                    library.binarySearchBook(searchBookId);
                    break;


                case 10:
                    // Exit
                    System.out.println("Exiting the system.");
                    scanner.close();
                    return;

                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    // Custom method to convert a string to an integer
    public static int convertToInt(String str) {
        int result = 0;
        boolean isNegative = false;
        int startIndex = 0;

        // Handle negative numbers
        if (str.charAt(0) == '-') {
            isNegative = true;
            startIndex = 1; // Start conversion from the second character
        }

        for (int i = startIndex; i < str.length(); i++) {
            char c = str.charAt(i);
            // Check if the character is a digit
            if (c < '0' || c > '9') {
                return -1; // Return -1 if it's not a valid number
            }
            result = result * 10 + (c - '0');
        }

        return isNegative ? -result : result; // Return the negative number if needed
    }
}
