// Auto generated code, do not modify
package qoober.http.callers;

import qoober.http.APICall;

public class StartForgingCall extends APICall.Builder<StartForgingCall> {
    private StartForgingCall() {
        super(ApiSpec.startForging);
    }

    public static StartForgingCall create() {
        return new StartForgingCall();
    }

    public StartForgingCall secretPhrase(String secretPhrase) {
        return param("secretPhrase", secretPhrase);
    }
}
