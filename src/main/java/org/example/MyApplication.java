package org.example;

import io.undertow.Undertow;
import io.undertow.servlet.Servlets;
import org.jboss.resteasy.core.ResteasyDeploymentImpl;
import org.jboss.resteasy.plugins.server.undertow.UndertowJaxrsServer;
import org.jboss.weld.environment.servlet.Listener;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

@ApplicationPath("/")
public class MyApplication extends Application {
  @Override
  public Set<Class<?>> getClasses() {
    var classes = new HashSet<Class<?>>();
    classes.add(MyResource.class);
    return classes;
  }

  public static void main(String[] args) {
    //
    // Configure deployment.
    //
    var deployment = new ResteasyDeploymentImpl();
    deployment.setApplicationClass(MyApplication.class.getName());
    deployment.setInjectorFactoryClass("org.jboss.resteasy.cdi.CdiInjectorFactory"); // set CDI injector factory

    var server = new UndertowJaxrsServer();
    var deploymentInfo = server.undertowDeployment(deployment, "/");
    deploymentInfo.setClassLoader(Application.class.getClassLoader());
    deploymentInfo.setDeploymentName("Default");
    deploymentInfo.setContextPath("/");
    deploymentInfo.addListener(Servlets.listener(Listener.class));
    server.deploy(deploymentInfo);

    //
    // Start Undertow server.
    //
    var runningOnAppEngine = System.getenv().containsKey("GAE_SERVICE");
    var listenPort = Integer.parseInt(System.getenv().getOrDefault("PORT", "8080"));

    var builder = Undertow
        .builder()
        .addHttpListener(
            listenPort,
            runningOnAppEngine ? "0.0.0.0" : "localhost");
    server.start(builder);
  }
}