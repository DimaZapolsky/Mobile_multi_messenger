package ru.sirius.january.mmm;

import android.support.annotation.Nullable;

import ru.sirius.january.mmm.data.abstracts.Dialog;
import ru.sirius.january.mmm.data.abstracts.Message;

public interface APIHelper {
    void loadChats(int count, @Nullable Dialog lastDialog);
    void loadMessages(Dialog dialog, int count, @Nullable Message lastMessage);
    void startPolling();
    void stopPolling();
}
