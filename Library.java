    import java.io.*;
    import java.util.Stack;

    public class Library {
        private Book[] books;
        private int bookCount;
        private Activity[] activities;
        private int activityCount;
        private Stack<UndoOperation> undoStack;// Stack for undo operations
        private ActivityFile activityFile;


        // Constructor
        public Library(int capacity) {
            books = new Book[capacity];
            activities = new Activity[capacity];
            bookCount = 0;
            activityCount = 0;
            undoStack = new Stack<>();// Initialize the stack with capacity
            loadFromBookFile();
            activityFile = new ActivityFile("activity_log.txt");

        }
        // Add a book (with dynamic resizing)
        public void addBook(Book book) {
            if (bookCount == books.length) {
                // Resize the array if it's full
                resizeBooksArray(books.length * 2);  // Double the size
            }

            books[bookCount] = book;
            bookCount++;
            undoStack.push(new UndoOperation("ADD", book));  // Push the added book onto the stack
            System.out.println("Book added successfully.");
            savetofileBook();
        }

        // Resize the books array dynamically
        private void resizeBooksArray(int newCapacity) {
            // Create a new array with the new capacity
            Book[] newArray = new Book[newCapacity];

            // Manually copy elements from the old array to the new array
            for (int i = 0; i < bookCount; i++) {
                newArray[i] = books[i];
            }

            // Update the books array to the new array
            books = newArray;
            System.out.println("Books array resized to new capacity: " + newCapacity);
        }
        // Remove a book
        public void removeBook(String bookId) {
            for (int i = 0; i < bookCount; i++) {
                if (manualStringEquals(books[i].getBookId(), bookId)) {
                    Book removedBook = books[i];  // Store the book to be removed
                    for (int j = i; j < bookCount - 1; j++) {
                        books[j] = books[j + 1]; // Shift left
                    }
                    books[--bookCount] = null;
                    undoStack.push(new UndoOperation("REMOVE", removedBook));  // Push the removed book onto the stack
                    System.out.println("Book removed successfully.");
                    savetofileBook();
                    return;
                }
            }
            System.out.println("Book not found.");
        }
        public void savetofileBook() {
            try(BufferedWriter f=new BufferedWriter(new FileWriter("books.txt")) ){


                for (int i = 0; i < bookCount; i++) {
                    f.write(books[i].getBookId() + ","+books[i].getTitle() + ","+ books[i].getAuthor() + ","+ (books[i].isBorrowed()? "Available" : "Not Available"));
                    f.newLine();
                }


            }
            catch (IOException e){
                System.out.println(e.getMessage());
            }

        }

        public void loadFromBookFile()  {
            try {
                File f = new File("books.txt");
                if(!f.exists()) {
                    f.createNewFile();
                }
                else {


                    FileReader fr= new FileReader(f);

                    BufferedReader s = new BufferedReader(fr);// file se read krega
                    String Line;
                    while ((Line=s.readLine())!=null){
                        String[] book= Line.split(",");//change
                        String Id=book[0];
                        String title = book[1];
                        String Author = book[2];
                        boolean avail=book[3].equalsIgnoreCase("AVAILABLE");//change
                        Book b= new Book(Id,title,Author);
                        b.setBorrowed(avail);
                        addBook(b);


                    }
                    s.close();


                } }catch (IOException e) {
                System.out.println( e.getMessage());
            }}


        // Display all books
        public void displayAllBooks() {
            System.out.println("\nBooks in the Library:");
            for (int i = 0; i < bookCount; i++) {
                System.out.println(books[i]);
            }
        }

        // Sort books by Book ID using Counting Sort
        public void countingSortBooks() {
            int max = 0;

            // Find the maximum Book ID value
            for (int i = 0; i < bookCount; i++) {
                int value = Integer.parseInt(books[i].getBookId());
                if (value > max) max = value;
            }

            Book[] output = new Book[bookCount];
            int[] count = new int[max + 1];

            // Count the occurrences
            for (int i = 0; i < bookCount; i++) {
                count[manualStringToInt(books[i].getBookId())]++;
            }

            // Update count array
            for (int i = 1; i <= max; i++) {
                count[i] += count[i - 1];
            }

            // Build the output array
            for (int i = bookCount - 1; i >= 0; i--) {
                int value = manualStringToInt(books[i].getBookId());
                output[count[value] - 1] = books[i];
                count[value]--;
            }

            // Copy sorted books back to the original array
            for (int i = 0; i < bookCount; i++) {
                books[i] = output[i];
            }
            savetofileBook();

            System.out.println("Books sorted by Book ID using Counting Sort.");
        }

        // Binary search for Book ID
        public void binarySearchBook(String bookId) {
            int left = 0, right = bookCount - 1;

            while (left <= right) {
                int mid = (left + right) / 2;
                String midBookId = books[mid].getBookId();

                if (manualStringEquals(midBookId, bookId)) {
                    System.out.println("Book found: " + books[mid]);
                    return;
                } else if (manualStringCompare(midBookId, bookId) < 0) {
                    left = mid + 1;
                } else {
                    right = mid - 1;
                }
            }

            System.out.println("Book not found.");
        }

        // Borrow a book
        public void borrowBook(String bookId, String borrowerName) {
            for (int i = 0; i < bookCount; i++) {
                if (manualStringEquals(books[i].getBookId(), bookId) && !books[i].isBorrowed()) {
                    books[i].setBorrowed(true);
                    savetofileBook();
                    undoStack.push(new UndoOperation("BORROW", books[i]));

                    // Record the borrow time
                    String borrowTime = getCurrentDateTime();
                    activities[activityCount] = new Activity(bookId, borrowerName, borrowTime, "");
                    activityCount++;

                    // Write the activity to the file
                    activityFile.writeLine(activities[activityCount - 1].toString());  // Use the last added activity

                    System.out.println("Book borrowed successfully.");
                    return;
                }
            }
            System.out.println("Book not available or already borrowed.");
        }


        // Undo the last operation (Add, Remove, Borrow)
        public void undoLastOperation() {
            if (manualIsEmpty(undoStack)) {
                System.out.println("No operation to undo.");
                return;
            }

            UndoOperation lastOperation = undoStack.pop();
            if (lastOperation != null) {
                System.out.println("Undoing the last operation: " + lastOperation.getOperationType());

                // Perform the correct undo operation based on the type
                switch (lastOperation.getOperationType()) {
                    case "ADD":
                        // Undo adding a book: Remove the last added book
                        removeBook(lastOperation.getBook().getBookId());
                        savetofileBook();
                        break;
                    case "REMOVE":
                        // Undo removing a book: Re-add the removed book
                        addBook(lastOperation.getBook());
                        savetofileBook();
                        break;
                    case "BORROW":
                        // Undo borrowing a book: Mark it as returned
                        returnBook(lastOperation.getBook().getBookId());
                        savetofileBook();
                        break;
                    default:
                        System.out.println("Unknown operation.");
                }
            }
        }
        // Return a book
        public void returnBook(String bookId) {
            for (int i = 0; i < bookCount; i++) {
                if (manualStringEquals(books[i].getBookId(), bookId) && books[i].isBorrowed()) {
                    books[i].setBorrowed(false);

                    // Record the return time
                    String returnTime = getCurrentDateTime();
                    boolean found = false;
                    for (int j = 0; j < activityCount; j++) {
                        if (manualStringEquals(activities[j].getBookId(), bookId) && activities[j].getEndTime().isEmpty()) {
                            activities[j].setEndTime(returnTime);
                            found = true;
                            activityFile.writeLine(activities[j].toString());  // Update the activity log file
                            break;
                        }
                    }

                    if (found) {
                        System.out.println("Book returned successfully.");
                        savetofileBook();
                    } else {
                        System.out.println("Return activity not found or already returned.");
                    }
                    return;
                }
            }
            System.out.println("Book not found or already returned.");
        }
       /* public void displayActivityLogFile() {
            activityFile.readAllLines();
        }
    */
       public void displayActivityLog() {
           System.out.println("\nActivity Log:");
           for (int i = 0; i < activityCount; i++) {
               System.out.println(activities[i]);
           }
       }


        // Get current date and time (manual implementation without built-in classes)
        private String getCurrentDateTime() {
            // Get current time in milliseconds
            long currentTimeMillis = System.currentTimeMillis();

            // Calculate seconds, minutes, hours, day, month, and year manually
            long seconds = currentTimeMillis / 1000;
            long minutes = seconds / 60;
            long hours = minutes / 60;
            long days = hours / 24;

            int year = 1970 + (int) (days / 365);
            int month = (int) ((days % 365) / 30) + 1;
            int day = (int) ((days % 365) % 30) + 1;

            int currentSeconds = (int) (seconds % 60);
            int currentMinutes = (int) (minutes % 60);
            int currentHours = (int) (hours % 24);

            return String.format("%04d-%02d-%02d %02d:%02d:%02d", year, month, day, currentHours, currentMinutes, currentSeconds);
        }

        // Manual string equals comparison
        private boolean manualStringEquals(String str1, String str2) {
            if (str1 == null || str2 == null) return false;
            if (str1.length() != str2.length()) return false;
            for (int i = 0; i < str1.length(); i++) {
                if (str1.charAt(i) != str2.charAt(i)) return false;
            }
            return true;
        }

        // Manual string comparison (like compareTo)
        private int manualStringCompare(String str1, String str2) {
            int len1 = str1.length();
            int len2 = str2.length();
            int minLength = Math.min(len1, len2);

            for (int i = 0; i < minLength; i++) {
                if (str1.charAt(i) < str2.charAt(i)) return -1;
                if (str1.charAt(i) > str2.charAt(i)) return 1;
            }
            if (len1 < len2) return -1;
            if (len1 > len2) return 1;
            return 0;
        }

        // Manual conversion of string to int (without Integer.parseInt)
        private int manualStringToInt(String str) {
            int result = 0;
            for (int i = 0; i < str.length(); i++) {
                result = result * 10 + (str.charAt(i) - '0');
            }
            return result;
        }

        // Manual check for stack empty condition
        private boolean manualIsEmpty(Stack<?> stack) {
            return stack.size() == 0;
        }
    }
