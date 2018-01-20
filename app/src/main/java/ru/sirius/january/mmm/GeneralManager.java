package ru.sirius.january.mmm;


import android.content.Context;
import android.os.AsyncTask;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.TreeMap;

import ru.sirius.january.mmm.data.abstracts.Dialog;
import ru.sirius.january.mmm.data.abstracts.Message;
import ru.sirius.january.mmm.data.abstracts.User;
import ru.sirius.january.mmm.vk.VKHelper;

public final class GeneralManager {

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

    synchronized public void addMessage(Message message, Dialog dialog) {
        messages.put(message.getUniqueID(), message);
        if (message.compareTo(dialog.getLast()) == 1) {
            dialog.setLast(message);
            updateChat(dialog);
        }
        int pos = Collections.binarySearch(dialog.getMessages(), message);
        if (pos < 0)
            pos = -pos - 1;
        dialog.getMessages().add(pos, message);
        if (currentDialog != null && currentDialog.uniqueID == dialog.uniqueID) {
            for (int i = pos; i < dialog.getMessages().size(); ++i)
                dialogCallback.onMessageUpdate(i);
        }
    }

    synchronized public void updateMessage(Message message, Dialog dialog) {
        if (currentDialog != null && currentDialog.uniqueID == dialog.uniqueID) {
            int pos = Collections.binarySearch(dialog.getMessages(), message);
            dialogCallback.onMessageUpdate(pos);
        }
    }

    public void addUser(User user) {
        users.put(user.getUniqueID(), user);
    }

    public void updateUser(User user) {

    }


    synchronized public void addChat(Dialog dialog) {
        dialogs.put(dialog.getUniqueID(), dialog);
        int pos = Collections.binarySearch(dialogList, dialog);
        if (pos < 0)
            pos = -pos - 1;
        dialogList.add(pos, dialog);
        if (dialogListFragment != null) {
            for (int i = pos; i < dialogList.size(); ++i)
                dialogListFragment.OnDialogUpdate(pos);
        }
    }

    synchronized public void updateChat(Dialog dialog) {
        //TODO: optimise
        Arrays.sort(dialogList.toArray());
        if (dialogListFragment != null) {
            for (int i = 0; i < dialogList.size(); ++i)
                dialogListFragment.OnDialogUpdate(i);
        }
    }

    public Message getMessageByID(int id) {
        return messages.get(id);
    }

    public User getUserByID(int id) {
        return users.get(id);
    }

    public Dialog getChatByID(int id) {
        return dialogs.get(id);
    }

    private VKHelper vkHelper = null;

    public void startVKTracking() throws VKHelper.VKUnauthorizedException {
        vkHelper = VKHelper.getInstance(context, this);
        vkHelper.loadChats(100, null);
        vkHelper.startPolling();
    }

    public void stopVKTracking() {
        try {
            vkHelper.stopPolling();
            vkHelper = null;
        } catch (NullPointerException ignored) {}
    }

    volatile private DialogListFragment dialogListFragment = null;

    public void setDialogsCallback(DialogListFragment dialogListFragment) {
        this.dialogListFragment = dialogListFragment;
    }

    public int getDialogsNumber()
    {
        return dialogList.size();
    }

    public Dialog getDialogByPosition(int position) {
        return dialogList.get(position);
    }

    volatile private Dialog currentDialog = null;
    volatile private DialogViewFragment dialogCallback = null;

    public void setDialogCallback(DialogViewFragment callback, Dialog dialog) {
        dialogCallback = callback;
        currentDialog = dialog;
        if (dialog != null)
            dialog.loadOld();
    }

    public Message getMessageByPosition(int position) {
        return currentDialog.getMessages().get(position);
    }

    public int getMessagesNumber() {
        return currentDialog.getMessages().size();
    }

    public void sendMessage(Dialog dialog, String text, ArrayList attachments) {

    }
}
