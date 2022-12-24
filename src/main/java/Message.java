interface Message {
    int ID_IDX = 0;
    int PARENT_ID_IDX = ID_IDX + 1;
    int COMMAND_IDX = PARENT_ID_IDX + 1;
    int ARGS_FROM_IDX = COMMAND_IDX + 1;

    int getCnt();
    void setId(int id);
    int getId();
    void setParentId(int parentId);
    int getParentId();
    void setCommand(String command);
    String getCommand();
    void setArguments(String[] args);
    String[] getArguments();
    void setArgument(int idx, String arg);
    String getArgument(int idx);
    String toString();
    void extendArguments(String[] data);
}
