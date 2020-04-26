// ChatRegister.aidl
package www.mys.com.ourtalkandroid;

import www.mys.com.ourtalkandroid.ChatCallBack;

// Declare any non-default types here with import statements

interface ChatRegister {

     void register(ChatCallBack callback);

     void unRegister(ChatCallBack callback);

     int send(int type, String data);

}
