package qoober.http;

import org.json.simple.JSONObject;
import org.json.simple.JSONStreamAware;
import qoober.Account;
import qoober.QooberException;

import javax.servlet.http.HttpServletRequest;

public final class GetLastParaminingStart extends APIServlet.APIRequestHandler {

    static final GetLastParaminingStart instance = new GetLastParaminingStart();

    private GetLastParaminingStart() {
        super(new APITag[] {APITag.ACCOUNTS, APITag.PARAMINING}, "account");
    }

    @Override
    protected JSONStreamAware processRequest(HttpServletRequest req) throws QooberException {

        Account account = ParameterParser.getAccount(req);

        JSONObject response = new JSONObject();
        if (account == null)
            response.put("lastParaminingStartBlock", "0");
        else
            response.put("lastParaminingStartBlock", String.valueOf(account.getLastParaminingStart()));

        return response;
    }
}