package ru.sirius.january.mmm;


import android.content.Context;

import java.util.ArrayList;
import java.util.Collections;
import java.util.TreeMap;

import ru.sirius.january.mmm.data.abstracts.Dialog;
import ru.sirius.january.mmm.data.abstracts.Message;
import ru.sirius.january.mmm.data.abstracts.User;
import ru.sirius.january.mmm.vk.VKHelper;

public final class GeneralManager implements Core {

    private volatile static GeneralManager instance = null;

    private GeneralManager(Context context) {
        this.context = context;
    }

    synchronized public static GeneralManager getInstance(Context context) {
        if (instance == null)
            instance = new GeneralManager(context);
        return instance;
    }

    Context context;
    volatile private TreeMap<Integer, User> users = new TreeMap<>();
    volatile private TreeMap<Integer, Message> messages = new TreeMap<>();
    volatile private TreeMap<Integer, Dialog> dialogs = new TreeMap<>();
    volatile private ArrayList<Dialog> dialogList = new ArrayList<>();

    @Override
    public void addMessage(Message message) {
        messages.put(message.getUniqueID(), message);

    }

    @Override
    public void updateMessage(Message message) {

    }

    @Override
    public void addUser(User user) {
        users.put(user.getUniqueID(), user);
    }

    @Override
    public void updateUser(User user) {

    }

    @Override
    synchronized public void addChat(Dialog dialog) {
        dialogs.put(dialog.getUniqueID(), dialog);
        int pos = Collections.binarySearch(dialogList, dialog);
        if (pos < 0)
            pos = -pos - 1;
        dialogList.add(pos, dialog);
        if (dialogListFragment != null) {
            dialogListFragment.OnDialogUpdate(pos);
        }
    }

    @Override
    public void updateChat(Dialog dialog) {

    }

    @Override
    public Message getMessageByID(int id) {
        return messages.get(id);
    }

    @Override
    public User getUserByID(int id) {
        return users.get(id);
    }

    @Override
    public Dialog getChatByID(int id) {
        return dialogs.get(id);
    }

    private VKHelper vkHelper = null;

    public void startVKTracking() throws VKHelper.VKUnauthorizedException {
        vkHelper = VKHelper.getInstance(context, this);
        vkHelper.loadChats(200, null);
        vkHelper.startPolling();
    }

    public void stopVKTracking() {
        try {
            vkHelper.stopPolling();
            vkHelper = null;
        } catch (NullPointerException ignored) {}
    }

    DialogListFragment dialogListFragment = null;

    public void setCallback(DialogListFragment dialogListFragment) {
        this.dialogListFragment = dialogListFragment;
    }

    public int getDialogsNumber()
    {
        return dialogList.size();
    }

    public Dialog getDialogByPosition(int position)
    {
        return dialogList.get(position);
    }
}
