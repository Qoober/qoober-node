package qoober;

import java.math.BigDecimal;

public class Checkpoint {
    private Long accountBalance = null;
    private BigDecimal structCoefficient = null;
    private BigDecimal storageCoefficient = null;
    private BigDecimal paratax = null;
    private Integer blocks = null;
    private boolean reInvest = false;

    public Checkpoint() {}

    public Checkpoint(long balance, long structBalance) {
        accountBalance = balance;
        structCoefficient = Paramining.getStructCoefficient(structBalance);
    }

    public Long getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(Long accountBalance) {
        this.accountBalance = accountBalance;
    }

    public void addAccountBalance(Long investBalance) {
        this.accountBalance += investBalance;
    }

    public BigDecimal getStructCoefficient() {
        return structCoefficient;
    }

    public void setStructCoefficient(BigDecimal structCoefficient) {
        this.structCoefficient = structCoefficient;
    }

    public BigDecimal getStorageCoefficient() {
        return storageCoefficient;
    }

    public void setStorageCoefficient(BigDecimal storageCoefficient) {
        this.storageCoefficient = storageCoefficient;
    }

    public BigDecimal getParatax() {
        return paratax;
    }

    public void setParatax(BigDecimal paratax) {
        this.paratax = paratax;
    }

    public Integer getBlocks() {
        return blocks;
    }

    public void setBlocks(Integer blocks) {
        this.blocks = blocks;
    }

    public boolean isReInvest() {
        return reInvest;
    }

    public void setReInvest(boolean reInvest) {
        this.reInvest = reInvest;
    }

    long calculate() {
        BigDecimal balanceCoefficient = null;
        for (int i = 0; i < Paramining.balanceLevels.length - 1; i++)
            if (accountBalance >= Paramining.balanceLevels[i] && accountBalance < Paramining.balanceLevels[i + 1])
                balanceCoefficient = Paramining.balanceCoefficients[i];

        if (balanceCoefficient == null)
            return 0;

        return BigDecimal.valueOf(accountBalance)
                .multiply(balanceCoefficient)
                .multiply(structCoefficient)
                .multiply(storageCoefficient)
                .multiply(paratax)
                .multiply(BigDecimal.valueOf(blocks))
                .longValue();
    }

    public String toString() {
        return "{ Balance: " + ((accountBalance != null) ? Long.toUnsignedString(accountBalance) : "NULL") +
               ", structCoefficient: " + ((structCoefficient != null) ? structCoefficient.toString() : "NULL") +
               ", storageCoefficient: " + ((storageCoefficient != null) ? storageCoefficient.toString() : "NULL") +
               ", paratax: " + ((paratax != null) ? paratax.toString() : "NULL") +
               ", blocks: " + ((blocks != null) ? blocks : "NULL") +
               ", reInvest: " + reInvest + " }";
    }
}
