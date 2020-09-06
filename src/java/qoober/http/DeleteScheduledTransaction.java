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

import qoober.Transaction;
import qoober.TransactionScheduler;
import qoober.util.JSON;
import org.json.simple.JSONStreamAware;

import javax.servlet.http.HttpServletRequest;

public final class DeleteScheduledTransaction extends APIServlet.APIRequestHandler {

    static final DeleteScheduledTransaction instance = new DeleteScheduledTransaction();

    private DeleteScheduledTransaction() {
        super(new APITag[] {APITag.TRANSACTIONS, APITag.ACCOUNTS}, "transaction");
    }

    @Override
    protected JSONStreamAware processRequest(HttpServletRequest req) throws ParameterException {

        long transactionId = ParameterParser.getUnsignedLong(req, "transaction", true);
        Transaction transaction = TransactionScheduler.deleteScheduledTransaction(transactionId);
        return transaction == null ? JSON.emptyJSON : JSONData.unconfirmedTransaction(transaction);
    }

    @Override
    protected boolean requirePost() {
        return true;
    }

    @Override
    protected boolean requireFullClient() {
        return true;
    }

    @Override
    protected boolean requirePassword() {
        return true;
    }

    @Override
    protected boolean allowRequiredBlockParameters() {
        return false;
    }

}
