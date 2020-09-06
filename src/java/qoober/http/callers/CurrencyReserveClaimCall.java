// Auto generated code, do not modify
package qoober.http.callers;

public class CurrencyReserveClaimCall extends CreateTransactionCallBuilder<CurrencyReserveClaimCall> {
    private CurrencyReserveClaimCall() {
        super(ApiSpec.currencyReserveClaim);
    }

    public static CurrencyReserveClaimCall create() {
        return new CurrencyReserveClaimCall();
    }

    public CurrencyReserveClaimCall currency(String currency) {
        return param("currency", currency);
    }

    public CurrencyReserveClaimCall currency(long currency) {
        return unsignedLongParam("currency", currency);
    }

    public CurrencyReserveClaimCall units(long units) {
        return param("units", units);
    }
}
