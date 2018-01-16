package ru.sirius.january.mmm;


import ru.sirius.january.mmm.data.abstracts.Dialog;
import ru.sirius.january.mmm.data.abstracts.Message;
import ru.sirius.january.mmm.data.abstracts.User;

public interface Core {
    void addMessage(Message message);
    void updateMessage(Message message);
    void addUser(User user);
    void updateUser(User user);
    void addChat(Dialog dialog);
    void updateChat(Dialog dialog);
    Message getMessageByID(int id);
    User getUserByID(int id);
    Dialog getChatByID(int id);
}
