package com.technologies.resource;

import com.technologies.exception.AccountTransferException;
import com.technologies.model.User;
import com.technologies.service.UserService;
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
import java.util.List;

/**
 * Resources for user.
 */
@Slf4j
@Path("/user")
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {

    @Inject
    UserService userService;

    /**
     * API - /User/1234 for get user by id
     *
     * @param userId {@link Long}
     *
     * @return User
     *
     * @throws AccountTransferException
     */
    @GET
    @Path("/{userId}")
    public User getUserById(@PathParam("userId") Long userId) throws AccountTransferException {
        log.info("Getting user by id");
        User user = userService.getUserById(userId);

        if (user == null) {
            throw new WebApplicationException("User with id: " + userId + " not found", Response.Status.NOT_FOUND);
        }
        return user;
    }

    /**
     * API - /User for get all users
     *
     * @return List of users
     *
     * @throws AccountTransferException
     */
    @GET
    public List<User> getAllUsers() throws AccountTransferException {
        log.info("Getting all users");

        return userService.getAllUsers();
    }

    /**
     * API - /User/add for add new user
     *
     * @param user {@link User}
     *
     * @return Response
     *
     * @throws AccountTransferException
     */
    @POST
    @Path("/add")
    public Response addUser(User user) throws AccountTransferException {
        log.info("Adding new user ...");
        if (userService.getUserById(user.getUserId()) != null) {
            throw new WebApplicationException("User already exist", Response.Status.BAD_REQUEST);
        }
        userService.addUser(user);
        return Response.status(Response.Status.OK).build();
    }

    /**
     * API - /User/userId for update user
     *
     * @param userId {@link Long}
     * @param user {@link User}
     *
     * @return Response
     *
     * @throws AccountTransferException
     */
    @PUT
    @Path("/{userId}")
    public Response updateUser(@PathParam("userId") Long userId, User user) throws AccountTransferException {
        userService.updateUser(userId, user);
        return Response.status(Response.Status.OK).build();
    }

    /**
     * API - /User/userId for delete user
     *
     * @param userId {@link Long}
     *
     * @return Response
     *
     * @throws AccountTransferException
     */
    @DELETE
    @Path("/{userId}")
    public Response deleteUser(@PathParam("userId") long userId) throws AccountTransferException {
        userService.deleteUser(userId);
        return Response.status(Response.Status.OK).build();
    }

}
