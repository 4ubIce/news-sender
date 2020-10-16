import controller.SubscriberController;

public class MainSubscriber {

    public static void main(String[] args) {
        new SubscriberController(args[0], Integer.parseInt(args[1]));
    }

}
