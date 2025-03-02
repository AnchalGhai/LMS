public class Stack {
    private Book[] stackArray;
    private int top;
    private int capacity;

    // Constructor
    public Stack(int capacity) {
        this.capacity = capacity;
        stackArray = new Book[capacity];
        top = -1;
    }

    // Push an element onto the stack
    public void push(Book book) {
        if (top == capacity - 1) {
            System.out.println("Stack is full.");
        } else {
            stackArray[++top] = book;
        }
    }

    // Pop an element from the stack
    public Book pop() {
        if (top == -1) {
            System.out.println("Stack is empty.");
            return null;
        } else {
            return stackArray[top--];
        }
    }

    // Check if the stack is empty
    public boolean isEmpty() {
        return top == -1;
    }

    // Peek the top element of the stack
    public Book peek() {
        if (top == -1) {
            System.out.println("Stack is empty.");
            return null;
        } else {
            return stackArray[top];
        }
    }
}
