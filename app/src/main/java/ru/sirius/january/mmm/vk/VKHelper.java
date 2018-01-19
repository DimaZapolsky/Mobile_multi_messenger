package ru.sirius.january.mmm.vk;


import android.content.Context;
import android.support.annotation.Nullable;

import com.vk.sdk.VKAccessToken;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import ru.sirius.january.mmm.GeneralManager;
import ru.sirius.january.mmm.data.UniqueIDBuilder;
import ru.sirius.january.mmm.data.abstracts.Dialog;
import ru.sirius.january.mmm.data.abstracts.Message;
import ru.sirius.january.mmm.data.vk.VKDialog;
import ru.sirius.january.mmm.data.vk.VKMessage;
import ru.sirius.january.mmm.data.vk.VKUser;

public final class VKHelper {
    private static VKHelper object = null;

    private Context context;
    private GeneralManager core;
    private VKUser me;

    public class VKUnauthorizedException extends Exception {
    }

    private VKHelper(Context context, GeneralManager core) throws VKUnauthorizedException {
        this.context = context;
        this.core = core;
        VKAccessToken token = VKAccessToken.tokenFromFile(context.getCacheDir().getAbsolutePath() + "/vkToken");
        if (token == null)
            throw new VKUnauthorizedException();
        me = new VKUser(Integer.parseInt(token.userId));
    }

    public static VKHelper getInstance(Context context, GeneralManager core) throws VKUnauthorizedException {
        if (object == null)
            object = new VKHelper(context, core);
        return object;
    }

    public void loadUsers(final ArrayList<VKUser> users)
    {
        ArrayList<Integer> ids = new ArrayList<>();
        for (VKUser user : users)
            ids.add(user.id);
        final VKRequest request = VKApi.users().get(VKParameters.from(VKApiConst.USER_IDS, Arrays.toString(ids.toArray()), VKApiConst.FIELDS, Arrays.toString((new String[]{"photo_100", "online", "last_seen"}))));
        request.executeSyncWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);
                try {
                    JSONArray items = response.json.getJSONArray("response");
                    for (int i = 0; i < users.size(); ++i) {
                        users.get(i).fromJson(items.getJSONObject(i));
                        GeneralManager.getInstance(null).updateUser(users.get(i));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void loadChats(int count, @Nullable Dialog lastDialog) {
        while (count > 0) {
            VKRequest request;
            if (lastDialog == null)
                request = new VKRequest("messages.getDialogs", VKParameters.from(VKParameters.from(VKApiConst.COUNT, Math.min(200, count))));
            else
                request = new VKRequest("messages.getDialogs", VKParameters.from(VKParameters.from(VKApiConst.COUNT, Math.min(200, count), VKApiConst.START_MESSAGE_ID, ((VKMessage) lastDialog.getLast()).getId())));
            final ArrayList<VKDialog> dialogsToUpdate = new ArrayList<>();
            final ArrayList<VKUser> usersToLoad = new ArrayList<>();
            request.executeSyncWithListener(new VKRequest.VKRequestListener() {
                @Override
                public void onComplete(VKResponse response) {
                    super.onComplete(response);
                    try {
                        JSONArray items = response.json.getJSONObject("response").getJSONArray("items");
                        for (int i = 0; i < items.length(); ++i) {
                            JSONObject msg = items.getJSONObject(i).getJSONObject("message");
                            int with_id = msg.getInt("user_id");
                            VKUser with = (VKUser)core.getUserByID(UniqueIDBuilder.VK(with_id));
                            if (with == null) {
                                with = new VKUser(with_id);
                                core.addUser(with);
                            }
                            VKUser owner = with;
                            if (msg.getInt("out") == 1) {
                                owner = me;
                            }
                            int msg_id = msg.getInt("id");
                            VKMessage vkMessage = (VKMessage)core.getMessageByID(msg_id);
                            boolean newMessage = false;
                            if (vkMessage == null) {
                                newMessage = true;
                                vkMessage = new VKMessage(msg_id, msg.getString("body"), new Date(msg.getLong("date")), owner, msg.getInt("out") == 1);
                            }
                            int chat_id = msg.getInt("user_id");
                            boolean hasTitle = false;
                            if (msg.has("chat_id")) {
                                chat_id = 2000000000 + msg.getInt("chat_id");
                                hasTitle = true;
                            }
                            VKDialog chat = (VKDialog)core.getChatByID(chat_id);
                            String title = with.getName();
                            String image = with.getImageKey();
                            int unread = 0;
                            try {
                                unread = items.getJSONObject(i).getInt("unread");
                            } catch (JSONException ignored) {}
                            if (hasTitle) {
                                try {
                                    image = msg.getString("photo_100");
                                } catch (JSONException e) {
                                    image = null;
                                }
                                title = msg.getString("title");
                            }
                            if (chat == null) {
                                chat = new VKDialog(chat_id, title, image, vkMessage);
                                core.addChat(chat);
                            } else {
                                chat.setName(title);
                                chat.setLast(vkMessage);
                            }
                            if (!hasTitle) {
                                usersToLoad.add(with);
                                dialogsToUpdate.add(chat);
                            }
                            if (newMessage) {
                                core.addMessage(vkMessage, chat);
                            }
                            chat.setUnread(unread);
                            core.updateChat(chat);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
            loadUsers(usersToLoad);
            for (int i = 0; i < dialogsToUpdate.size(); ++i) {
                dialogsToUpdate.get(i).setName(usersToLoad.get(i).getName());
                dialogsToUpdate.get(i).setImageKey(usersToLoad.get(i).getImageKey());
                GeneralManager.getInstance(null).updateChat(dialogsToUpdate.get(i));
            }
            count -= Math.min(200, count);
        }
    }

    public void startPolling() {

    }

    public void stopPolling() {

    }
}
