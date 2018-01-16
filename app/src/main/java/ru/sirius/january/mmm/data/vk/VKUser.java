package ru.sirius.january.mmm.data.vk;

import android.util.Log;

import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;

import ru.sirius.january.mmm.data.UniqueIDBuilder;
import ru.sirius.january.mmm.data.abstracts.User;


public final class VKUser extends User {
    public int id;
    public VKUser(int id) {
        super(UniqueIDBuilder.VK(id));
        this.id = id;
        updateAll();
        updateOnline();
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
                    setOnline(res.getBoolean("online"));
                    setLastSeen(new Date(res.getLong("time")));
                } catch (JSONException e) {
                    Log.e("VK", "Parsing JSON", e);
                }
            }
        });
    }

    @Override
    public void updateAll() {
        /*VKRequest request = VKApi.users().get(VKParameters.from(VKApiConst.USER_IDS, id, VKApiConst.FIELDS, "photo_50"));
        request.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);
                try {
                    setName(response.json.getString("first_name") + " " + response.json.getString("second_name"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    setImageKey(new URI(response.json.getString("photo_50")));
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
            }
        });*/
    }
}
