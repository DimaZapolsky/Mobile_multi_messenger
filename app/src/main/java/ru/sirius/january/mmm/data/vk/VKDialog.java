package ru.sirius.january.mmm.data.vk;


import java.net.URI;
import java.util.ArrayList;

import ru.sirius.january.mmm.data.UniqueIDBuilder;
import ru.sirius.january.mmm.data.abstracts.Dialog;
import ru.sirius.january.mmm.data.abstracts.Message;

public final class VKDialog extends Dialog {
    int id = -1;

    public VKDialog(int id, String name, String image, Message last) {
        super(UniqueIDBuilder.VK(id), new ArrayList<Message>(), name, image, last);
        this.id = id;
    }

    @Override
    public void update() {

    }

    @Override
    public void loadOld() {
        
    }
}
