package org.example;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

@RequestScoped
@Path("/")
public class MyResource {
  @Inject
  BeanManager manager;

  @Path("/")
  @GET
  public String getManager() {
    return manager.toString();
  }
}
