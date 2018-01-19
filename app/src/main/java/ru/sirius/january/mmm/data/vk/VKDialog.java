package ru.sirius.january.mmm.data.vk;


import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;

import ru.sirius.january.mmm.GeneralManager;
import ru.sirius.january.mmm.data.UniqueIDBuilder;
import ru.sirius.january.mmm.data.abstracts.Dialog;
import ru.sirius.january.mmm.data.abstracts.Message;
import ru.sirius.january.mmm.vk.VKHelper;

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
        VKRequest request;
        if (getMessages().size() == 0)
            request = new VKRequest("messages.getHistory", VKParameters.from(VKApiConst.OFFSET, 0, VKApiConst.COUNT, 10, "peer_id", id));
        else
            request = new VKRequest("messages.getHistory", VKParameters.from(VKApiConst.START_MESSAGE_ID, ((VKMessage) getMessages().get(0)).getId(), VKApiConst.OFFSET, 0, VKApiConst.COUNT, 10, "peer_id", id, "user_id", ""));
        request.getPreparedParameters().put("v", "5.52");
        final VKDialog dialog = this;
        final ArrayList<VKMessage> messagesToUpdate = new ArrayList<>();
        final HashSet<VKUser> usersToLoad = new HashSet<>();
        request.executeSyncWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);
                try {
                    JSONArray items = response.json.getJSONObject("response").getJSONArray("items");
                    int size = items.length();
                    for (int i = 0; i < size; ++i) {
                        JSONObject item = items.getJSONObject(i);
                        VKMessage message = (VKMessage) GeneralManager.getInstance(null).getMessageByID(UniqueIDBuilder.VK(item.getInt("id")));
                        VKUser owner = (VKUser) GeneralManager.getInstance(null).getUserByID(UniqueIDBuilder.VK(item.getInt("from_id")));
                        if (owner == null) {
                            owner = new VKUser(item.getInt("from_id"));
                            GeneralManager.getInstance(null).addUser(owner);
                        }
                        if (message == null) {
                            message = new VKMessage(item.getInt("id"), item.getString("body"), new Date(item.getLong("date")), owner, item.getInt("out") == 1);
                            message.setRead(item.getInt("read_state") == 1);
                            GeneralManager.getInstance(null).addMessage(message, dialog);
                        } else {
                            message.setText(item.getString("body"));
                            message.setRead(item.getInt("read_state") == 1);
                            GeneralManager.getInstance(null).updateMessage(message, dialog);
                        }
                        if (!owner.Loaded) {
                            if (!usersToLoad.contains(owner)) {
                                usersToLoad.add(owner);
                            }
                            messagesToUpdate.add(message);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(VKError error) {
                super.onError(error);
            }
        });
        try {
            VKHelper.getInstance(null, null).loadUsers(new ArrayList<VKUser>(usersToLoad));
            for (int i = 0; i < messagesToUpdate.size(); ++i)
                GeneralManager.getInstance(null).updateMessage(messagesToUpdate.get(i), dialog);
        } catch (VKHelper.VKUnauthorizedException e) {
            e.printStackTrace();
        }
    }
}
