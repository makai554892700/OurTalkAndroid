package www.mys.com.ourtalkandroid.utils.data;

import java.util.HashMap;

import www.mys.com.ourtalkandroid.base.StaticParam;
import www.mys.com.ourtalkandroid.utils.ResolverUtils;

public class JsonHeardUtils {

    private static final JsonHeardUtils jsonHeardUtils = new JsonHeardUtils();
    private HashMap<String, String> heard = new HashMap<>();
    private static final String SESSION_START = "JSESSIONID=";

    private JsonHeardUtils() {
        heard.put("Content-Type", "application/json");
        heard.put("mark", ResolverUtils.getInstance().getStringSetting(StaticParam.SESSION));
    }

    public static JsonHeardUtils getInstance() {
        return jsonHeardUtils;
    }

    public HashMap<String, String> getHeard() {
        return heard;
    }

    public void addSession(String session) {
        if (session != null) {
            session = session.substring(session.indexOf(SESSION_START) + SESSION_START.length()
                    , session.indexOf(";"));
        }
        if (session != null) {
            ResolverUtils.getInstance().saveSetting(StaticParam.SESSION, session);
            heard.put("mark", session);
        }
    }

    public void delSession() {
        ResolverUtils.getInstance().saveSetting(StaticParam.SESSION, null);
        heard.put("mark", null);
    }

}
