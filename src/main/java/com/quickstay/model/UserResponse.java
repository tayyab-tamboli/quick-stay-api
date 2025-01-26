package com.quickstay.model;

public class UserResponse {
    private Response response;
    private User user;

    public UserResponse(Response response, User user) {
        this.response = response;
        this.user = user;
    }

    public UserResponse() {

    }

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
