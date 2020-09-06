package qoober.http;

import org.json.simple.JSONObject;
import org.json.simple.JSONStreamAware;
import qoober.Account;
import qoober.QooberException;
import qoober.util.Convert;

import javax.servlet.http.HttpServletRequest;

public final class GetAccountParent extends APIServlet.APIRequestHandler {

    static final GetAccountParent instance = new GetAccountParent();

    private GetAccountParent() {
        super(new APITag[] {APITag.ACCOUNTS, APITag.PARAMINING}, "account");
    }

    @Override
    protected JSONStreamAware processRequest(HttpServletRequest req) throws QooberException {

        Account account = ParameterParser.getAccount(req);

        JSONObject response = new JSONObject();
        if (account == null)
            response.put("parent", "null");
        else
            response.put("parent", Convert.rsAccount(account.getParent()));

        return response;
    }
}
