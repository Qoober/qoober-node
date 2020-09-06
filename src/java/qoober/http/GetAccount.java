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
import qoober.Account;
import qoober.Paramining;
import qoober.QooberException;
import qoober.db.DbIterator;
import qoober.util.Convert;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONStreamAware;

import javax.servlet.http.HttpServletRequest;

public final class GetAccount extends APIServlet.APIRequestHandler {

    static final GetAccount instance = new GetAccount();

    private GetAccount() {
        super(new APITag[] {APITag.ACCOUNTS}, "account", "includeLessors", "includeAssets", "includeCurrencies", "includeEffectiveBalance", "includeParamining");
    }

    @Override
    protected JSONStreamAware processRequest(HttpServletRequest req) throws QooberException {

        Account account = ParameterParser.getAccount(req);
        boolean includeLessors = "true".equalsIgnoreCase(req.getParameter("includeLessors"));
        boolean includeAssets = "true".equalsIgnoreCase(req.getParameter("includeAssets"));
        boolean includeCurrencies = "true".equalsIgnoreCase(req.getParameter("includeCurrencies"));
        boolean includeEffectiveBalance = "true".equalsIgnoreCase(req.getParameter("includeEffectiveBalance"));
        boolean includeParamining = "true".equalsIgnoreCase(req.getParameter("includeParamining"));

        JSONObject response = JSONData.accountBalance(account, includeEffectiveBalance);
        JSONData.putAccount(response, "account", account.getId());

        byte[] publicKey = Account.getPublicKey(account.getId());
        if (publicKey != null) {
            response.put("publicKey", Convert.toHexString(publicKey));
        }
        Account.AccountInfo accountInfo = account.getAccountInfo();
        if (accountInfo != null) {
            response.put("name", Convert.nullToEmpty(accountInfo.getName()));
            response.put("description", Convert.nullToEmpty(accountInfo.getDescription()));
        }
        Account.AccountLease accountLease = account.getAccountLease();
        if (accountLease != null) {
            JSONData.putAccount(response, "currentLessee", accountLease.getCurrentLesseeId());
            response.put("currentLeasingHeightFrom", accountLease.getCurrentLeasingHeightFrom());
            response.put("currentLeasingHeightTo", accountLease.getCurrentLeasingHeightTo());
            if (accountLease.getNextLesseeId() != 0) {
                JSONData.putAccount(response, "nextLessee", accountLease.getNextLesseeId());
                response.put("nextLeasingHeightFrom", accountLease.getNextLeasingHeightFrom());
                response.put("nextLeasingHeightTo", accountLease.getNextLeasingHeightTo());
            }
        }

        if (!account.getControls().isEmpty()) {
            JSONArray accountControlsJson = new JSONArray();
            account.getControls().forEach(accountControl -> accountControlsJson.add(accountControl.toString()));
            response.put("accountControls", accountControlsJson);
        }

        if (includeLessors) {
            try (DbIterator<Account> lessors = account.getLessors()) {
                if (lessors.hasNext()) {
                    JSONArray lessorIds = new JSONArray();
                    JSONArray lessorIdsRS = new JSONArray();
                    JSONArray lessorInfo = new JSONArray();
                    while (lessors.hasNext()) {
                        Account lessor = lessors.next();
                        lessorIds.add(Long.toUnsignedString(lessor.getId()));
                        lessorIdsRS.add(Convert.rsAccount(lessor.getId()));
                        lessorInfo.add(JSONData.lessor(lessor, includeEffectiveBalance));
                    }
                    response.put("lessors", lessorIds);
                    response.put("lessorsRS", lessorIdsRS);
                    response.put("lessorsInfo", lessorInfo);
                }
            }
        }

        if (includeAssets) {
            try (DbIterator<Account.AccountAsset> accountAssets = account.getAssets(0, -1)) {
                JSONArray assetBalances = new JSONArray();
                JSONArray unconfirmedAssetBalances = new JSONArray();
                while (accountAssets.hasNext()) {
                    Account.AccountAsset accountAsset = accountAssets.next();
                    JSONObject assetBalance = new JSONObject();
                    assetBalance.put("asset", Long.toUnsignedString(accountAsset.getAssetId()));
                    assetBalance.put("balanceQNT", String.valueOf(accountAsset.getQuantityQNT()));
                    assetBalances.add(assetBalance);
                    JSONObject unconfirmedAssetBalance = new JSONObject();
                    unconfirmedAssetBalance.put("asset", Long.toUnsignedString(accountAsset.getAssetId()));
                    unconfirmedAssetBalance.put("unconfirmedBalanceQNT", String.valueOf(accountAsset.getUnconfirmedQuantityQNT()));
                    unconfirmedAssetBalances.add(unconfirmedAssetBalance);
                }
                if (assetBalances.size() > 0) {
                    response.put("assetBalances", assetBalances);
                }
                if (unconfirmedAssetBalances.size() > 0) {
                    response.put("unconfirmedAssetBalances", unconfirmedAssetBalances);
                }
            }
        }

        if (includeCurrencies) {
            try (DbIterator<Account.AccountCurrency> accountCurrencies = account.getCurrencies(0, -1)) {
                JSONArray currencyJSON = new JSONArray();
                while (accountCurrencies.hasNext()) {
                    currencyJSON.add(JSONData.accountCurrency(accountCurrencies.next(), false, true));
                }
                if (currencyJSON.size() > 0) {
                    response.put("accountCurrencies", currencyJSON);
                }
            }
        }
				
				if(includeParamining){
					response.put("paraminingVirtualBalanceNQT", account.getVirtualParaminingBalanceNQT());
					response.put("paraminingStructBalanceNQT", account.getStructBalanceNQT(Qoober.getBlockchain().getHeight()) );
					response.put("paraminingStructCoefficient", Paramining.getAccountStructCoefficient(account.getId()) );
					response.put("paraminingStorageDays", Paramining.getAccountStorageDays(account.getId()) );
					response.put("paraminingStorageCoefficient", Paramining.getAccountStorageCoefficient(account.getId()) );
					response.put("paraminingBalanceCoefficient", String.valueOf(Paramining.getAccountBalanceCoefficient(account.getId())) );
					response.put("paraminingDegradeCoefficient", Paramining.getDegradeCoefficient());
				}

        return response;

    }

}
