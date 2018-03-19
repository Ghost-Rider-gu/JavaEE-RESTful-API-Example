package com.technologies.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.technologies.model.User;
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
import java.net.URI;
import java.net.URISyntaxException;

import static org.junit.Assert.assertTrue;

/**
 * Tests for user service.
 */
public class UserServiceTest extends ServiceTest {

    private URIBuilder uriBuilder = new URIBuilder().setScheme("http").setHost("localhost:8088");
    private ObjectMapper mapper = new ObjectMapper();

    private final String WRONG_USER_ID = "/User/12345";
    private final String GET_ALL_USERS = "/User";
    private final String GET_USER_BY_NAME = "/User/John Cook";
    private final String UPDATE_USER = "/User/1";
    private final String ADD_USER = "/User/add";
    private final String DELETE_USER = "/User/1";

    @Test
    public void testShouldNotGetUserById() throws IOException, URISyntaxException {
        URI uri = uriBuilder.setPath(WRONG_USER_ID).build();
        HttpGet request = new HttpGet(uri);
        HttpResponse response = httpClient.execute(request);
        int statusCode = response.getStatusLine().getStatusCode();

        assertTrue(statusCode == 404);
    }

    @Test
    public void testGetAllUsers() throws IOException, URISyntaxException {
        URI uri = uriBuilder.setPath(GET_ALL_USERS).build();
        HttpGet request = new HttpGet(uri);
        HttpResponse response = httpClient.execute(request);
        int statusCode = response.getStatusLine().getStatusCode();

        assertTrue(statusCode == 200);

        String result = EntityUtils.toString(response.getEntity());
        User[] users = mapper.readValue(result, User[].class);

        assertTrue(users.length > 0);
    }

    @Test
    public void testAddUser() throws IOException, URISyntaxException {
        URI uri = uriBuilder.setPath(ADD_USER).build();
        User newUser = new User();
        newUser.setUserName("Alien");
        newUser.setUserPhone("12-456-688");
        newUser.setUserEmail("alien@space.com");

        String jsonInString = mapper.writeValueAsString(newUser);
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
        URI uri = uriBuilder.setPath(UPDATE_USER).build();
        User currentUser = new User();
        currentUser.setUserId(1L);
        currentUser.setUserName("John Cook");
        currentUser.setUserPhone("23345-5657-445");
        currentUser.setUserEmail("cook@gmail.com");

        String jsonInString = mapper.writeValueAsString(currentUser);
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
        URI uri = uriBuilder.setPath(DELETE_USER).build();
        HttpDelete request = new HttpDelete(uri);
        request.setHeader("Content-type", "application/json");
        HttpResponse response = httpClient.execute(request);
        int statusCode = response.getStatusLine().getStatusCode();

        assertTrue(statusCode == 200);
    }

}
