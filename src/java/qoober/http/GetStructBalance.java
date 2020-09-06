package qoober.http;

import org.json.simple.JSONObject;
import org.json.simple.JSONStreamAware;
import qoober.Account;
import qoober.Qoober;
import qoober.QooberException;

import javax.servlet.http.HttpServletRequest;

public final class GetStructBalance extends APIServlet.APIRequestHandler {

    static final GetStructBalance instance = new GetStructBalance();

    private GetStructBalance() {
        super(new APITag[] {APITag.ACCOUNTS, APITag.PARAMINING}, "account", "numberOfConfirmations");
    }

    @Override
    protected JSONStreamAware processRequest(HttpServletRequest req) throws QooberException {

        Account account = ParameterParser.getAccount(req);
        int numberOfConfirmations = ParameterParser.getNumberOfConfirmations(req);

        JSONObject response = new JSONObject();
        if (account == null)
            response.put("structBalanceNQT", "0");
        else
            response.put("structBalanceNQT", String.valueOf(account.getStructBalanceNQT(Qoober.getBlockchain().getHeight() - numberOfConfirmations)));

        return response;
    }
}