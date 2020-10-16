import controller.PublisherController;
import view.PublisherUI;

public class MainPublisher {

    public static void main(String[] args) {
        new PublisherController(Integer.parseInt(args[0]));
    }

}
