/*
 * Copyright © 2013-2016 The Qoober Core Developers.
 * Copyright © 2016-2020 Jelurida IP B.V.
 *
 * See the LICENSE.txt file at the top-level directory of this distribution
 * for licensing information.
 *
 * Unless otherwise agreed in a custom licensing agreement with Jelurida B.V.,
 * no part of the Qoober software, including this file, may be copied, modified,
 * propagated, or distributed except according to the terms contained in the
 * LICENSE.txt file.
 *
 * Removal or modification of this copyright notice is prohibited.
 *
 */

package qoober.http;

import qoober.Qoober;
import qoober.QooberException;
import qoober.Transaction;
import qoober.Account;
import qoober.Genesis;
import qoober.db.DbIterator;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONStreamAware;

import javax.servlet.http.HttpServletRequest;

public final class GetExplorerStats extends APIServlet.APIRequestHandler {

    static final GetExplorerStats instance = new GetExplorerStats();

    private GetExplorerStats() {
        super(new APITag[] {APITag.TRANSACTIONS}, "firstIndex", "lastIndex");
    }
		
    @Override
    protected JSONStreamAware processRequest(HttpServletRequest req) throws QooberException {
				long newAllBalance = Math.abs(Account.getAccount(Genesis.CREATOR_ID).getBalanceNQT());
			
        JSONObject response = new JSONObject();
        response.put("transactionCount", String.valueOf(Qoober.getBlockchain().getTransactionCount()));
        response.put("emissionTotalNQT", String.valueOf(newAllBalance));
				
        return response;

    }

}
