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
import qoober.db.DbIterator;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONStreamAware;

import javax.servlet.http.HttpServletRequest;

public final class GetAllTransactions extends APIServlet.APIRequestHandler {

    static final GetAllTransactions instance = new GetAllTransactions();

    private GetAllTransactions() {
        super(new APITag[] {APITag.TRANSACTIONS}, "firstIndex", "lastIndex");
    }

    @Override
    protected JSONStreamAware processRequest(HttpServletRequest req) throws QooberException {
				int firstIndex = ParameterParser.getFirstIndex(req);
        int lastIndex = ParameterParser.getLastIndex(req);

        JSONArray transactions = new JSONArray();
        try (DbIterator<? extends Transaction> iterator = Qoober.getBlockchain().getAllTransactions(firstIndex, lastIndex)) {
            while (iterator.hasNext()) {
                Transaction transaction = iterator.next();
                transactions.add(JSONData.transaction(transaction, false));
            }
        }
				
				/*
				JSONArray transactionsReverse = new JSONArray();
				for (int i = transactions.size()-1; i>=0; i--) {
						transactionsReverse.add(transactions.get(i));
				}
				*/

        JSONObject response = new JSONObject();
        response.put("count", Qoober.getBlockchain().getTransactionCount());
        response.put("transactions", transactions);
        return response;

    }

}
