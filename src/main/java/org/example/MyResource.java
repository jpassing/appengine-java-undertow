package org.example;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

@RequestScoped
@Path("/")
public class MyResource {
  @Path("/")
  @GET
  public String index() {
    return "Hello world";
  }
}
