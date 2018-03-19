package com.technologies.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.technologies.model.Account;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;

import static org.junit.Assert.assertTrue;

/**
 * Tests for account service
 */
public class AccountServiceTest extends ServiceTest {

    private URIBuilder uriBuilder = new URIBuilder().setScheme("http").setHost("localhost:8088");
    private ObjectMapper mapper = new ObjectMapper();

    private final String WRONG_ACCOUNT_ID = "/Account/12345";
    private final String GET_ALL_ACCOUNTS = "/Account";
    private final String UPDATE_ACCOUNT = "/Account/1";
    private final String ADD_ACCOUNT = "/Account/add";
    private final String DELETE_ACCOUNT = "/Account/1";
    private final String TRANSFER_BALANCE = "/Account/transfer/125";

    @Test
    public void testShouldNotGetAccountById() throws IOException, URISyntaxException {
        URI uri = uriBuilder.setPath(WRONG_ACCOUNT_ID).build();
        HttpGet request = new HttpGet(uri);
        HttpResponse response = httpClient.execute(request);
        int statusCode = response.getStatusLine().getStatusCode();

        assertTrue(statusCode == 404);
    }

    @Test
    public void testGetAllAccounts() throws IOException, URISyntaxException {
        URI uri = uriBuilder.setPath(GET_ALL_ACCOUNTS).build();
        HttpGet request = new HttpGet(uri);
        HttpResponse response = httpClient.execute(request);
        int statusCode = response.getStatusLine().getStatusCode();

        assertTrue(statusCode == 200);

        String result = EntityUtils.toString(response.getEntity());
        Account[] accounts = mapper.readValue(result, Account[].class);

        assertTrue(accounts.length > 0);
    }

    @Test
    public void testAddUser() throws IOException, URISyntaxException {
        URI uri = uriBuilder.setPath(ADD_ACCOUNT).build();
        Account newAccount = new Account();
        newAccount.setAccountNumber("23453");
        newAccount.setAccountBalance(new BigDecimal(345.678));

        String jsonInString = mapper.writeValueAsString(newAccount);
        StringEntity entity = new StringEntity(jsonInString);
        HttpPost request = new HttpPost(uri);
        request.setHeader("Content-type", "application/json");
        request.setEntity(entity);
        HttpResponse response = httpClient.execute(request);
        int statusCode = response.getStatusLine().getStatusCode();

        assertTrue(statusCode == 200);
    }

    @Test
    public void testUpdateUser() throws IOException, URISyntaxException {
        URI uri = uriBuilder.setPath(UPDATE_ACCOUNT).build();
        Account currentAccount = new Account();
        currentAccount.setAccountId(1L);
        currentAccount.setAccountNumber("235345");
        currentAccount.setAccountBalance(new BigDecimal(567.45));

        String jsonInString = mapper.writeValueAsString(currentAccount);
        StringEntity entity = new StringEntity(jsonInString);
        HttpPut request = new HttpPut(uri);
        request.setHeader("Content-type", "application/json");
        request.setEntity(entity);
        HttpResponse response = httpClient.execute(request);
        int statusCode = response.getStatusLine().getStatusCode();

        assertTrue(statusCode == 200);
    }

    @Test
    public void testDeleteUser() throws IOException, URISyntaxException {
        URI uri = uriBuilder.setPath(DELETE_ACCOUNT).build();
        HttpDelete request = new HttpDelete(uri);
        request.setHeader("Content-type", "application/json");
        HttpResponse response = httpClient.execute(request);
        int statusCode = response.getStatusLine().getStatusCode();

        assertTrue(statusCode == 200);
    }

}
