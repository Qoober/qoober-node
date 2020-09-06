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

package qoober;

import qoober.db.BasicDb;
import qoober.db.TransactionalDb;

public final class Db {

    public static final String PREFIX = Constants.isTestnet ? "qoober.testDb" : "qoober.db";
    public static final TransactionalDb db = new TransactionalDb(new BasicDb.DbProperties()
            .maxCacheSize(Qoober.getIntProperty("qoober.dbCacheKB"))
            .dbUrl(Qoober.getStringProperty(PREFIX + "Url"))
            .dbType(Qoober.getStringProperty(PREFIX + "Type"))
            .dbDir(Qoober.getStringProperty(PREFIX + "Dir"))
            .dbParams(Qoober.getStringProperty(PREFIX + "Params"))
            .dbUsername(Qoober.getStringProperty(PREFIX + "Username"))
            .dbPassword(Qoober.getStringProperty(PREFIX + "Password", null, true))
            .maxConnections(Qoober.getIntProperty("qoober.maxDbConnections"))
            .loginTimeout(Qoober.getIntProperty("qoober.dbLoginTimeout"))
            .defaultLockTimeout(Qoober.getIntProperty("qoober.dbDefaultLockTimeout") * 1000)
            .maxMemoryRows(Qoober.getIntProperty("qoober.dbMaxMemoryRows"))
    );

    public static void init() {
        db.init(new QooberDbVersion());
    }

    static void shutdown() {
        db.shutdown();
    }

    private Db() {} // never

}
