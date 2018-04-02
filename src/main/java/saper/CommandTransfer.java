package saper;

public class CommandTransfer {
    private Board listener;
    private static CommandTransfer ourInstance = new CommandTransfer();

    public static CommandTransfer getInstance() {
        return ourInstance;
    }

    public void setListener(Board listener) {
        this.listener = listener;
    }

    public void doFinish (boolean victory) {
        listener.finishGame(true);
    }
}
