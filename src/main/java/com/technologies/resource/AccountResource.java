package com.technologies.resource;

import com.technologies.exception.AccountTransferException;
import com.technologies.model.Account;
import com.technologies.service.AccountService;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.POST;
import javax.ws.rs.DELETE;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.PathParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.util.List;

/**
 * Resources for account.
 */
@Path("/Account")
@Produces(MediaType.APPLICATION_JSON)
@Slf4j
public class AccountResource {

    @Inject
    AccountService accountService;

    /**
     * API - /Account/1234 for get account by id
     *
     * @param accountId {@link Long}
     *
     * @return Account
     *
     * @throws AccountTransferException
     */
    @GET
    @Path("/{accountId}")
    public Account getAccountById(@PathParam("accountId") Long accountId) throws AccountTransferException {
        log.info("Getting account by id");

        Account account = accountService.getAccountById(accountId);

        if (account == null) {
            throw new WebApplicationException("Account with id: " + accountId + " not found", Response.Status.NOT_FOUND);
        }
        return account;
    }

    /**
     * API - /Account for get all accounts
     *
     * @return List of accounts
     *
     * @throws AccountTransferException
     */
    @GET
    public List<Account> getAllUsers() throws AccountTransferException {
        log.info("Getting all accounts");

        return accountService.getAllAccounts();
    }

    /**
     * API - /Account/add for add new account
     *
     * @param account {@link Account}
     *
     * @return Response
     *
     * @throws AccountTransferException
     */
    @POST
    @Path("/add")
    public Response addAccount(Account account) throws AccountTransferException {
        log.info("Adding new account ...");
        if (accountService.getAccountById(account.getAccountId()) != null) {
            throw new WebApplicationException("Account already exist", Response.Status.BAD_REQUEST);
        }
        accountService.addAccount(account);
        return Response.status(Response.Status.OK).build();
    }

    /**
     * API - /Account/accountId for update account
     *
     * @param accountId {@link Long}
     * @param account {@link Account}
     *
     * @return Response
     *
     * @throws AccountTransferException
     */
    @PUT
    @Path("/{accountId}")
    public Response updateAccount(@PathParam("accountId") Long accountId, Account account) throws AccountTransferException {
        accountService.updateAccount(accountId, account);
        return Response.status(Response.Status.OK).build();
    }

    /**
     * API - /Account/accountId for delete account
     *
     * @param accountId {@link Long}
     *
     * @return Response
     *
     * @throws AccountTransferException
     */
    @DELETE
    @Path("/{accountId}")
    public Response deleteAccount(@PathParam("accountId") long accountId) throws AccountTransferException {
        accountService.deleteAccount(accountId);
        return Response.status(Response.Status.OK).build();
    }

    @POST
    @Path("/transfer/{amount}")
    public Response transferBalance(@PathParam("amount") BigDecimal amount, Account fromAccount, Account toAccount) throws AccountTransferException {
        accountService.transferBalance(fromAccount, toAccount, amount);
        return Response.status(Response.Status.OK).build();
    }

}
