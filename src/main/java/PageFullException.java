public class PageFullException extends Exception {

    PageFullException(String s) {
        super(s);
    }

   PageFullException() {
        super("Page is full!");
   }
}
