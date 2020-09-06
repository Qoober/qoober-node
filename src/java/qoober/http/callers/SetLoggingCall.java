// Auto generated code, do not modify
package qoober.http.callers;

import qoober.http.APICall;

public class SetLoggingCall extends APICall.Builder<SetLoggingCall> {
    private SetLoggingCall() {
        super(ApiSpec.setLogging);
    }

    public static SetLoggingCall create() {
        return new SetLoggingCall();
    }

    public SetLoggingCall logLevel(String logLevel) {
        return param("logLevel", logLevel);
    }

    public SetLoggingCall communicationEvent(String... communicationEvent) {
        return param("communicationEvent", communicationEvent);
    }
}
