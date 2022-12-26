import java.util.concurrent.Callable;

public class GetUserCallable implements Callable {
    private final Message inMessage;

    public GetUserCallable(Message message) {
        inMessage = message;
    }

    @Override
    public Object call() throws Exception {
        User user = new User(inMessage.getArgument(0), inMessage.getArgument(1), inMessage.getArgument(2));
        return user;
    }
}
