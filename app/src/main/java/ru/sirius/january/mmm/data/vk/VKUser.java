package ru.sirius.january.mmm.data.vk;

import android.util.Log;

import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import ru.sirius.january.mmm.GeneralManager;
import ru.sirius.january.mmm.data.UniqueIDBuilder;
import ru.sirius.january.mmm.data.abstracts.User;


public final class VKUser extends User {
    public int id;
    protected boolean Loaded = false;
    public VKUser(int id) {
        super(UniqueIDBuilder.VK(id));
        this.id = id;
    }

    @Override
    public void updateOnline() {
        final VKRequest request = new VKRequest("messages.getLastActivity", VKParameters.from(VKApiConst.USER_ID, id));
        request.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);
                try {
                    JSONObject res = response.json.getJSONObject("response");
                    setOnline(res.getInt("online") == 1);
                    setLastSeen(new Date(res.getLong("time")));
                } catch (JSONException e) {
                    Log.e("VK", "Parsing JSON", e);
                }
            }
        });
        GeneralManager.getInstance(null).updateUser(this);
    }

    @Override
    public void updateAll() {
        VKRequest request = VKApi.users().get(VKParameters.from(VKApiConst.USER_IDS, id, VKApiConst.FIELDS, Arrays.toString((new String[]{"photo_100", "online", "last_seen"}))));
        request.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);
                try {
                    JSONObject res = response.json.getJSONArray("response").getJSONObject(0);
                    fromJson(res);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        GeneralManager.getInstance(null).updateUser(this);
    }

    public void fromJson(JSONObject res)
    {
        try {
            setName(res.getString("first_name") + " " + res.getString("last_name"));
            setOnline(res.getInt("online") == 1);
            setLastSeen(new Date(res.getJSONObject("last_seen").getLong("time")));
            setImageKey(res.getString("photo_100"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
