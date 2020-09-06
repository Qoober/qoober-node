package qoober;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import qoober.util.Logger;

public class Paramining {

    static final long[] balanceLevels = {
            100L * Constants.ONE_QBR, 1000L * Constants.ONE_QBR, 10000L * Constants.ONE_QBR, 50000L * Constants.ONE_QBR,
            100000L * Constants.ONE_QBR, 500000L * Constants.ONE_QBR, 1000000L * Constants.ONE_QBR};

    static final BigDecimal[] balanceCoefficients = {
            new BigDecimal("0.000003125"), new BigDecimal("0.000002917"), new BigDecimal("0.000002708"),
            new BigDecimal("0.0000025"), new BigDecimal("0.000002292"), new BigDecimal("0.000002083")};

    private static final long[] parataxLevels = {
            1500000000L * Constants.ONE_QBR, 3000000000L * Constants.ONE_QBR, 4500000000L * Constants.ONE_QBR,
            6000000000L * Constants.ONE_QBR, 7000000000L * Constants.ONE_QBR};

    private static final BigDecimal[] parataxCoefficients = {
            new BigDecimal("0.9"), new BigDecimal("0.8"), new BigDecimal("0.6"), new BigDecimal("0.4"),
            BigDecimal.ZERO};

    private static final long[] structLevels = {
			10000L * Constants.ONE_QBR,
			50001L * Constants.ONE_QBR,
			250001L * Constants.ONE_QBR,
			500001L * Constants.ONE_QBR,
			1000001L * Constants.ONE_QBR,
			10000001L * Constants.ONE_QBR,
			100000001L * Constants.ONE_QBR
		};

    private static final BigDecimal[] structCoefficients = {
			new BigDecimal("1.1"),
			new BigDecimal("1.2"),
			new BigDecimal("1.3"),
			new BigDecimal("1.5"),
      new BigDecimal("1.7"),
			new BigDecimal("2"),
			new BigDecimal("2.2")
		};

    private static final int[] storageLevels = {14400, 43200, 86400, 129600, 216000, 259200, 525600};

    private static final BigDecimal[] storageCoefficients = {
			new BigDecimal("1.05"),
			new BigDecimal("1.1"),
			new BigDecimal("1.2"),
			new BigDecimal("1.4"),
			new BigDecimal("1.6"),
			new BigDecimal("1.8"),
			new BigDecimal("2.0")
		};

    private static BigDecimal getParatax(long allBalance) {
        for (int i = 0; i < parataxLevels.length; i++)
            if (allBalance >= parataxLevels[i] && (i == parataxLevels.length - 1 || allBalance < parataxLevels[i + 1]))
                return parataxCoefficients[i];

        return BigDecimal.ONE;
    }

    static BigDecimal getStructCoefficient(long structBalance) {
        for (int i = 0; i < structLevels.length; i++)
            if (structBalance >= structLevels[i] && (i == structLevels.length - 1 || structBalance < structLevels[i + 1]))
                return structCoefficients[i];

        return BigDecimal.ONE;
    }

    private static BigDecimal getStorageCoefficient(int storageHeight) {
        for (int i = 0; i < storageLevels.length; i++)
            if (storageHeight >= storageLevels[i] && (i == storageLevels.length - 1 || storageHeight < storageLevels[i + 1]))
                return storageCoefficients[i];

        return BigDecimal.ONE;
    }
		
		public static float getAccountBalanceCoefficient(long accountId){
			BigDecimal balanceCoefficient = null;
			long accountBalance = Account.getAccount(accountId).getBalanceNQT();
			
			for(int i = 0; i < balanceLevels.length - 1; i++){
				if(accountBalance >= balanceLevels[i] && accountBalance < balanceLevels[i + 1]){
					balanceCoefficient = balanceCoefficients[i];
				}
			}
			
			if(balanceCoefficient == null){
				return 0;
			}
			
			return balanceCoefficient
				.multiply(BigDecimal.valueOf(1440))
				.multiply(BigDecimal.valueOf(100))
				.floatValue();
		}
		public static long getAccountStorageDays(long accountId){
			int start = Account.getAccount(accountId).getLastParaminingStart();
      int end = Qoober.getBlockchain().getHeight();
			return (end - start) / 1440;
		}
		public static float getAccountStorageCoefficient(long accountId){
			int start = Account.getAccount(accountId).getLastParaminingStart();
      int end = Qoober.getBlockchain().getHeight();
			return getStorageCoefficient(end - start).floatValue();
		}
		public static long getAccountStructBalance(long accountId){
			long structBalance = 0;
			
			try (Connection con = Db.db.getConnection()) {
				try (PreparedStatement pstmt = con.prepareStatement(
                "SELECT struct_balance FROM account_paramining WHERE id = ? ORDER BY height DESC LIMIT 1"))
            {
                pstmt.setLong(1, accountId);
								
                try (ResultSet rs = pstmt.executeQuery()) {
                    if(rs.next()) structBalance = rs.getLong("struct_balance");
                }
            }
			} catch (SQLException e) {
					throw new RuntimeException(e.toString(), e);
			}
			
			return structBalance;
		}
		public static float getAccountStructCoefficient(long accountId){
			Account account = Account.getAccount(accountId);
			long structBalance = account.getStructBalanceNQT(Qoober.getBlockchain().getHeight());
			return getStructCoefficient(structBalance).floatValue();
		}
		public static float getDegradeCoefficient(){
			long allBalance = Math.abs(Account.getAccount(Genesis.CREATOR_ID).getBalanceNQT());
			return getParatax(allBalance).floatValue();
		}
		
    public static long calculate(long accountId) {
        int start = Account.getAccount(accountId).getLastParaminingStart();
        int end = Qoober.getBlockchain().getHeight();
        long reinvestedBalance = 0;
        long paraminingBalance = 0;
        TreeMap<Integer, Checkpoint> points = new TreeMap<>();

        try (Connection con = Db.db.getConnection()) {
            // add account checkpoints
            try (PreparedStatement pstmt = con.prepareStatement(
                "SELECT balance, struct_balance, height FROM account_paramining WHERE id = ? AND height >= ? AND height < ?"))
            {
                pstmt.setLong(1, accountId);
                pstmt.setInt(2, start);
                pstmt.setInt(3, end);

                try (ResultSet rs = pstmt.executeQuery()) {
                    while (rs.next()){
                      /*Logger.logDebugMessage("Paramaining points put height (%s), balance (%s)", String.valueOf(rs.getInt("height")), String.valueOf(rs.getLong("balance")));*/
											
											points.put(rs.getInt("height"), new Checkpoint(rs.getLong("balance"), rs.getLong("struct_balance")));
										}
                }

                if (points.isEmpty())
                    return 0;
            }
						
						start = points.firstEntry().getKey();
						/*
						if(start != points.firstKey()){
							start = points.firstKey();
						}
						*/

            // add paratax checkpoints
            try (PreparedStatement pstmt = con.prepareStatement(
                    "SELECT all_balance, height FROM paratax WHERE height < ? ORDER BY height ASC")) {
                pstmt.setInt(1, end);

                BigDecimal startParatax = BigDecimal.ONE;
                try (ResultSet rs = pstmt.executeQuery()) {
                    while (rs.next()) {
                        int height = rs.getInt("height");
                        BigDecimal paratax = getParatax(rs.getLong("all_balance"));
                        if (height <= start)
                            startParatax = paratax;
                        else
                            points.computeIfAbsent(height, k -> new Checkpoint()).setParatax(paratax);
                    }
                }
								
								/*
								Logger.logDebugMessage("Paramaining Calculate start: %s, paratax: %s",
                    String.valueOf(start), startParatax.toString());
								*/
								
                // set start paratax
								points.get( start ).setParatax(startParatax);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e.toString(), e);
        }

//        System.out.println("BASE LIST");
//        int y = start;
//        System.out.println(y + ": " + points.get(start).toString());
//        Map.Entry<Integer, Checkpoint> x;
//        while ((x = points.higherEntry(y)) != null) {
//            System.out.println(x.getKey() + ": " + x.getValue().toString());
//            y = x.getKey();
//        }

        // add reinvest checkpoints
        for (int h = start + Constants.PARAMINING_REINVEST_INTERVAL; h < end; h += Constants.PARAMINING_REINVEST_INTERVAL)
            points.computeIfAbsent(h, k -> new Checkpoint()).setReInvest(true);

//        System.out.println("REINVEST+ LIST");
//        y = start;
//        System.out.println(y + ": " + points.get(start).toString());
//        while ((x = points.higherEntry(y)) != null) {
//            System.out.println(x.getKey() + ": " + x.getValue().toString());
//            y = x.getKey();
//        }

        // fill missing data in checkpoints
        Checkpoint previousPoint = points.get(start);
        int previousHeight = start;
        Map.Entry<Integer, Checkpoint> current;
        while ((current = points.higherEntry(previousHeight)) != null) {
            Checkpoint currentPoint = current.getValue();
            if (currentPoint.getAccountBalance() == null)
                currentPoint.setAccountBalance(previousPoint.getAccountBalance());
            if (currentPoint.getStructCoefficient() == null)
                currentPoint.setStructCoefficient(previousPoint.getStructCoefficient());
            if (currentPoint.getParatax() == null)
                currentPoint.setParatax(previousPoint.getParatax());

            previousPoint.setBlocks(current.getKey() - previousHeight);
            previousPoint.setStorageCoefficient(getStorageCoefficient(current.getKey() - start));
            previousHeight = current.getKey();
            previousPoint = currentPoint;
        }

        previousPoint.setBlocks(end - previousHeight);
        previousPoint.setStorageCoefficient(getStorageCoefficient(end - start));

//        System.out.println("FILLED LIST");
//        y = start;
//        System.out.println(y + ": " + points.get(start).toString());
//        while ((x = points.higherEntry(y)) != null) {
//            System.out.println(x.getKey() + ": " + x.getValue().toString());
//            y = x.getKey();
//        }

        // calculation
        for (Checkpoint point: points.values()) {
//            System.out.println("Point: " + point.toString());
            if (point.isReInvest()) {
//                System.out.println("Reinvest! old = " + Long.toUnsignedString(reinvestedBalance) + " paramining = " + Long.toUnsignedString(paraminingBalance));
                reinvestedBalance += paraminingBalance;
//                System.out.println("Reinvest! new = " + Long.toUnsignedString(reinvestedBalance));
                paraminingBalance = 0;
            }
            point.addAccountBalance(reinvestedBalance);
            paraminingBalance += point.calculate();

//            long calculated = point.calculate();
//            System.out.println("Point calculated: " + Long.toUnsignedString(calculated));
//            paraminingBalance += calculated;
        }

        return reinvestedBalance + paraminingBalance;
    }

    public static void blockGenerateParaminingCheckpoints() {
        int blockchainHeight = Qoober.getBlockchain().getHeight();

        // check paratax changes
        long oldAllBalance = Math.abs(Account.getAccount(Genesis.CREATOR_ID, Math.max(blockchainHeight - 1, 0)).getBalanceNQT());
        long newAllBalance = Math.abs(Account.getAccount(Genesis.CREATOR_ID).getBalanceNQT());
        for (int i = parataxLevels.length - 1; i >= 0; i--)
            if (oldAllBalance < parataxLevels[i] && newAllBalance >= parataxLevels[i]) {
                // save paratax checkpoint
                try (Connection con = Db.db.getConnection();
                     PreparedStatement pstmt = con.prepareStatement("INSERT INTO paratax (all_balance, height) VALUES (?, ?)"))
                {
                    pstmt.setLong(1, parataxLevels[i]);
                    pstmt.setInt(2, blockchainHeight);
                    pstmt.executeUpdate();
                } catch (SQLException e) {
                    throw new RuntimeException(e.toString(), e);
                }
                break;
            }

        // check account changes
        Set<Long> updatedAccounts = new HashSet<>();
        Map<Long, Long> accountBalances = new HashMap<>();
        Map<Long, Long> structBalances = new HashMap<>();

        try (Connection con = Db.db.getConnection()) {
            try (PreparedStatement pstmt = con.prepareStatement(
                    "SELECT A1.id, A1.balance AS new_balance, ISNULL(A2.balance, 0) AS old_balance FROM account AS A1 " +
                        "LEFT JOIN account AS A2 ON A1.id = A2.id AND A2.height = " +
                        "(SELECT MAX(A3.height) FROM account AS A3 WHERE A3.id = A2.id AND A3.height < ?) WHERE A1.height = ?"))
            {
                pstmt.setInt(1, blockchainHeight);
                pstmt.setInt(2, blockchainHeight);

                try (ResultSet rs = pstmt.executeQuery()) {
                    while (rs.next()) {
                        long id = rs.getLong("id");
                        long newBalance = rs.getLong("new_balance");
                        long oldBalance = rs.getLong("old_balance");

                        if (newBalance != oldBalance &&
                            ((newBalance >= Constants.MIN_PARAMINING_BALANCE_QNT && newBalance < Constants.MAX_PARAMINING_BALANCE_QNT) ||
                             (oldBalance >= Constants.MIN_PARAMINING_BALANCE_QNT && oldBalance < Constants.MAX_PARAMINING_BALANCE_QNT)))
                        {
                            updatedAccounts.add(id);
                            accountBalances.put(id, newBalance);
                        }
                    }
                }
            }

            try (PreparedStatement pstmt = con.prepareStatement(
                    "SELECT A1.id, A1.balance AS new_balance, ISNULL(A2.balance, 0) AS old_balance FROM account_struct AS A1 " +
                        "LEFT JOIN account_struct AS A2 ON A1.id = A2.id AND A2.height = " +
                        "(SELECT MAX(A3.height) FROM account_struct AS A3 WHERE A3.id = A2.id AND A3.height < ?) WHERE A1.height = ?"))
            {
                pstmt.setInt(1, blockchainHeight);
                pstmt.setInt(2, blockchainHeight);

                try (ResultSet rs = pstmt.executeQuery()) {
                    while (rs.next()) {
                        long id = rs.getLong("id");
                        long newBalance = rs.getLong("new_balance");
                        long oldBalance = rs.getLong("old_balance");

                        if (newBalance != oldBalance) {
                            for (long level: structLevels)
                                if ((oldBalance < level && newBalance >= level) || (oldBalance >= level && newBalance < level)) {
                                    updatedAccounts.add(id);
                                    structBalances.put(id, newBalance);
                                    break;
                                }
                        }
                    }
                }
            }

            for (Long id: updatedAccounts) {
                Long balance = accountBalances.get(id);
                if (balance == null) {
                    try (PreparedStatement pstmt = con.prepareStatement(
                            "SELECT balance FROM account WHERE id = ? AND latest = TRUE"))
                    {
                        pstmt.setLong(1, id);

                        try (ResultSet rs = pstmt.executeQuery()) {
                            if (rs.next())
                                balance = rs.getLong("balance");
                            else
                                balance = 0L;
                        }
                    }
                }

                Long structBalance = structBalances.get(id);
                if (structBalance == null) {
                    try (PreparedStatement pstmt = con.prepareStatement(
                            "SELECT balance FROM account_struct WHERE id = ? AND latest = TRUE"))
                    {
                        pstmt.setLong(1, id);

                        try (ResultSet rs = pstmt.executeQuery()) {
                            if (rs.next())
                                structBalance = rs.getLong("balance");
                            else
                                structBalance = 0L;
                        }
                    }
                }

                // save account checkpoint
                try (PreparedStatement pstmtInsert = con.prepareStatement(
                        "INSERT INTO account_paramining (id, balance, struct_balance, height) VALUES (?, ?, ?, ?)")) {
                    pstmtInsert.setLong(1, id);
                    pstmtInsert.setLong(2, balance);
                    pstmtInsert.setLong(3, structBalance);
                    pstmtInsert.setInt(4, blockchainHeight);
                    pstmtInsert.executeUpdate();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e.toString(), e);
        }
    }
}