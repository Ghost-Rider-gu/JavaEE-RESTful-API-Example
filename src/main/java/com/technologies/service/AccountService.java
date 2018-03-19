package com.technologies.service;

import com.technologies.config.DbConfig;
import com.technologies.exception.AccountTransferException;
import com.technologies.model.Account;
import com.technologies.model.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.dbutils.DbUtils;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * CRUD for account entity.
 */
@Slf4j
public class AccountService {

    private final String GET_ACCOUNT_BY_ID = "SELECT * FROM Account WHERE accountId = ? ";
    private final String GET_ALL_ACCOUNTS = "SELECT * FROM Account";
    private final String ADD_NEW_ACCOUNT = "INSERT INTO Account (accountNumber, accountBalance) VALUES (?, ?)";
    private final String UPDATE_ACCOUNT = "UPDATE Account SET accountNumber = ?, accountBalance = ? WHERE accountId = ? ";
    private final String DELETE_ACCOUNT = "DELETE FROM Account WHERE accountId = ? ";
    private final static String TRANSFER_BALANCE = "SELECT * FROM Account WHERE accountId = ? FOR UPDATE";

    /**
     * Get account by ID
     *
     * @param accountId {@link Long}
     *
     * @return Account {@link User}
     *
     * @throws AccountTransferException
     */
    public Account getAccountById(Long accountId) throws AccountTransferException {
        Connection conn = null;
        PreparedStatement prepStatement = null;
        ResultSet resultSet = null;
        Account account = null;

        try {
            conn = DbConfig.getConnection();
            prepStatement = conn.prepareStatement(GET_ACCOUNT_BY_ID);
            prepStatement.setLong(1, accountId);
            resultSet = prepStatement.executeQuery();
            if (resultSet.next()) {
                account = new Account();
                account.setAccountId(resultSet.getLong("accountId"));
                account.setAccountNumber(resultSet.getString("accountNumber"));
                account.setAccountBalance(resultSet.getBigDecimal("accountBalance"));
            }
            return account;
        } catch (SQLException ex) {
            log.error("Can't find account with id: " + accountId);
            throw new AccountTransferException("Account with id: " + accountId + " not found. " + ex.getMessage());
        } finally {
            DbUtils.closeQuietly(conn, prepStatement, resultSet);
        }
    }

    /**
     * Get all accounts
     *
     * @return List of accounts
     *
     * @throws AccountTransferException
     */
    public List<Account> getAllAccounts() throws AccountTransferException {
        Connection conn = null;
        PreparedStatement prepStatement = null;
        ResultSet resultSet = null;
        Account account = null;
        List<Account> accounts = new ArrayList<>();

        try {
            conn = DbConfig.getConnection();
            prepStatement = conn.prepareStatement(GET_ALL_ACCOUNTS);
            resultSet = prepStatement.executeQuery();
            if (resultSet.next()) {
                account = new Account();
                account.setAccountId(resultSet.getLong("accountId"));
                account.setAccountNumber(resultSet.getString("accountNumber"));
                account.setAccountBalance(resultSet.getBigDecimal("accountBalance"));

                accounts.add(account);
            }
            return accounts;
        } catch (SQLException ex) {
            log.error("Can't get account data");
            throw new AccountTransferException("Account data: " + ex.getMessage());
        } finally {
            DbUtils.closeQuietly(conn, prepStatement, resultSet);
        }
    }

    /**
     * Add new account
     *
     * @param account {@link Account}
     *
     * @throws AccountTransferException
     */
    public void addAccount(Account account) throws AccountTransferException {
        Connection conn = null;
        PreparedStatement prepStatement = null;

        try {
            conn = DbConfig.getConnection();
            prepStatement = conn.prepareStatement(ADD_NEW_ACCOUNT);
            prepStatement.setString(1, account.getAccountNumber());
            prepStatement.setBigDecimal(2, account.getAccountBalance());
            int result = prepStatement.executeUpdate();
        } catch (SQLException ex) {
            log.error("Can't add new account");
            throw new AccountTransferException("Add account: " + ex.getMessage());
        } finally {
            DbUtils.closeQuietly(conn);
            DbUtils.closeQuietly(prepStatement);
        }
    }

    /**
     * Update current account
     *
     * @param accountId {@link Long}
     * @param account {@link Account}
     *
     * @throws AccountTransferException
     */
    public void updateAccount(Long accountId, Account account) throws AccountTransferException {
        Connection conn = null;
        PreparedStatement prepStatement = null;

        try {
            conn = DbConfig.getConnection();
            prepStatement = conn.prepareStatement(UPDATE_ACCOUNT);
            prepStatement.setString(1, account.getAccountNumber());
            prepStatement.setBigDecimal(2, account.getAccountBalance());
            prepStatement.setLong(3, accountId);
            int result = prepStatement.executeUpdate();
        } catch (SQLException ex) {
            log.error("Can't update account");
            throw new AccountTransferException("Update account: " + ex.getMessage());
        } finally {
            DbUtils.closeQuietly(conn);
            DbUtils.closeQuietly(prepStatement);
        }
    }

    /**
     * Delete current account
     *
     * @param accountId {@link Long}
     *
     * @throws AccountTransferException
     */
    public void deleteAccount(Long accountId) throws AccountTransferException {
        Connection conn = null;
        PreparedStatement prepStatement = null;

        try {
            conn = DbConfig.getConnection();
            prepStatement = conn.prepareStatement(DELETE_ACCOUNT);
            prepStatement.setLong(1, accountId);
            int result = prepStatement.executeUpdate();
        } catch (SQLException ex) {
            log.error("Can't delete account");
            throw new AccountTransferException("Delete account: " + ex.getMessage());
        } finally {
            DbUtils.closeQuietly(conn);
            DbUtils.closeQuietly(prepStatement);
        }
    }

    /**
     * Transfer balance between accounts
     *
     * @param fromAccountId {@link Account}
     * @param toAccountId {@link Account}
     * @param amount {@link BigDecimal}
     *
     * @throws AccountTransferException
     */
    public void transferBalance(Account fromAccountId, Account toAccountId, BigDecimal amount) throws AccountTransferException {
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        PreparedStatement updateStatement = null;
        ResultSet resultSet = null;
        Account fromAccount = null;
        Account toAccount = null;

        try {
            conn = DbConfig.getConnection();
            conn.setAutoCommit(false);
            // lock the credit and debit account for writing:
            preparedStatement = conn.prepareStatement(TRANSFER_BALANCE);
            preparedStatement.setLong(1, fromAccountId.getAccountId());
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                fromAccount = new Account();
                fromAccount.setAccountId(resultSet.getLong("accountId"));
                fromAccount.setAccountNumber(resultSet.getString("accountNumber"));
                fromAccount.setAccountBalance(resultSet.getBigDecimal("accountBalance"));
            }

            preparedStatement = conn.prepareStatement(TRANSFER_BALANCE);
            preparedStatement.setLong(1, toAccountId.getAccountId());
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                toAccount = new Account();
                toAccount.setAccountId(resultSet.getLong("accountId"));
                toAccount.setAccountNumber(resultSet.getString("accountNumber"));
                toAccount.setAccountBalance(resultSet.getBigDecimal("accountBalance"));
            }

            BigDecimal leftMoney = fromAccount.getAccountBalance().subtract(amount);
            if (leftMoney.compareTo(BigDecimal.ZERO) < 0) {
                throw new AccountTransferException("Account doesn't have enough money for transfer");
            }

            updateStatement = conn.prepareStatement(UPDATE_ACCOUNT);
            updateStatement.setString(1, fromAccountId.getAccountNumber());
            updateStatement.setBigDecimal(2, leftMoney);
            updateStatement.setLong(3, fromAccountId.getAccountId());
            updateStatement.addBatch();

            updateStatement.setString(2, fromAccountId.getAccountNumber());
            updateStatement.setBigDecimal(1, toAccount.getAccountBalance().add(amount));
            updateStatement.setLong(3, toAccount.getAccountId());
            updateStatement.addBatch();

            int[] rowsUpdated = updateStatement.executeBatch();

            conn.commit();
        } catch (SQLException ex) {
            log.error("Can't transfer money");
            throw new AccountTransferException("Transfer money: " + ex.getMessage());
        } finally {
            DbUtils.closeQuietly(conn);
            DbUtils.closeQuietly(preparedStatement);
            DbUtils.closeQuietly(updateStatement);
            DbUtils.closeQuietly(resultSet);
        }
    }

}
