package qoober.http;

import org.json.simple.JSONObject;
import org.json.simple.JSONStreamAware;
import qoober.Account;
import qoober.QooberException;

import javax.servlet.http.HttpServletRequest;

public final class GetVirtualParaminingBalance extends APIServlet.APIRequestHandler {

    static final GetVirtualParaminingBalance instance = new GetVirtualParaminingBalance();

    private GetVirtualParaminingBalance() {
        super(new APITag[] {APITag.ACCOUNTS, APITag.PARAMINING}, "account");
    }

    @Override
    protected JSONStreamAware processRequest(HttpServletRequest req) throws QooberException {

        Account account = ParameterParser.getAccount(req);

        JSONObject response = new JSONObject();
        if (account == null)
            response.put("virtualParaminingBalanceNQT", "0");
        else
            response.put("virtualParaminingBalanceNQT", String.valueOf(account.getVirtualParaminingBalanceNQT()));

        return response;
    }
}