package ru.sirius.january.mmm.telegram;

import android.util.Log;

import org.drinkless.td.libcore.telegram.Client;
import org.drinkless.td.libcore.telegram.TdApi;

public class TelegramHelper {

    private static TelegramHelper instance;

    private static String cache_dir;
    private static final String API_HASH = "bd13138bd25cb2ab25a5176ef2183c4f";
    private static final String DEVICE_MODEL = "unknown";
    private static final String VERSION = "1.0";
    private static final String LANG = "ru";
    private static final int API_ID = 150857;

    private Client client;

    public static void setCache_dir(String cache_dir) {
        TelegramHelper.cache_dir = cache_dir;
    }

    private TelegramHelper(String cache_dir) {
        try {
            client = Client.create(new Client.ResultHandler() {
                @Override
                public void onResult(TdApi.Object object) {
                    if (object instanceof TdApi.Error) {
                        Log.e("MyLog", ((TdApi.Error) object).message);
                    }
                }
            }, new Client.ExceptionHandler() {
                @Override
                public void onException(Throwable e) {
                    Log.e("MyLog", e.getMessage());
                }
            }, new Client.ExceptionHandler() {
                @Override
                public void onException(Throwable e) {
                    Log.e("MyLog", e.getMessage());
                }
            });
            TdApi.TdlibParameters tdlibParameters = new TdApi.TdlibParameters(false, cache_dir, cache_dir, false,
                    false, false, false,
                    API_ID, API_HASH, LANG,
                    DEVICE_MODEL, VERSION, VERSION,
                    false, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static TelegramHelper getInstance() {
        if (instance == null) {
            try {
                synchronized (TelegramHelper.class) {
                    if (instance == null) {
                        instance = new TelegramHelper(cache_dir);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return instance;
    }

    public void sendPhone(String phone) {
        try {
            TdApi.SetAuthenticationPhoneNumber smsSender =
                    new TdApi.SetAuthenticationPhoneNumber(phone, true, true);
            client.send(smsSender, new Client.ResultHandler() {
                @Override
                public void onResult(TdApi.Object object) {

                }
            }, new Client.ExceptionHandler() {
                @Override
                public void onException(Throwable e) {
                    Log.e("MyLog", e.getMessage());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendCode(String code) {
        try {
            TdApi.CheckAuthenticationCode checkCode =
                    new TdApi.CheckAuthenticationCode(code, null, null);
            client.send(checkCode, new Client.ResultHandler() {
                @Override
                public void onResult(TdApi.Object object) {

                }

            }, new Client.ExceptionHandler() {
                @Override
                public void onException(Throwable e) {
                    Log.e("MyLog", e.getMessage());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getAuthState() {
        try {
            client.send(new TdApi.GetAuthorizationState(), new Client.ResultHandler() {
                @Override
                public void onResult(TdApi.Object object) {

                }
            }, new Client.ExceptionHandler() {
                @Override
                public void onException(Throwable e) {
                    Log.e("MyLog", e.getMessage());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendMessage() {
        try {
            TdApi.SendMessage sendMessage = new TdApi.SendMessage();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}