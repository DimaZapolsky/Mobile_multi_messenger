package ru.sirius.january.mmm.vk;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKError;
import com.vk.sdk.util.VKUtil;

import ru.sirius.january.mmm.R;
import ru.sirius.january.mmm.store.StorageManager;

public class VkAuthActivity extends AppCompatActivity {

    public static final String VK_ACCESS_TOKEN = "VK_ACCESS_TOKEN";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_vk_auth);
        String[] fingerprints = VKUtil.getCertificateFingerprint(this, this.getPackageName());
        System.out.println(fingerprints[0]);
        VKSdk.login(this, "messages", "offline");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!VKSdk.onActivityResult(requestCode, resultCode, data, new VKCallback<VKAccessToken>() {
            @Override
            public void onResult(VKAccessToken res) {
                res.saveTokenToFile(getApplicationContext().getCacheDir().getAbsolutePath() + "/vkToken");
            }
            @Override
            public void onError(VKError error) {
                Log.e("Auth", error.toString());
            }
        })) {
            super.onActivityResult(requestCode, resultCode, data);
        }
        finish();
    }
}
