package org.example;

import io.undertow.Undertow;
import io.undertow.servlet.Servlets;
import io.undertow.servlet.api.DeploymentInfo;
import org.jboss.resteasy.core.ResteasyDeploymentImpl;
import org.jboss.resteasy.plugins.server.undertow.UndertowJaxrsServer;
import org.jboss.resteasy.spi.ResteasyDeployment;
import org.jboss.weld.environment.servlet.Listener;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.net.http.WebSocket;
import java.util.HashSet;
import java.util.Set;

@ApplicationPath("/")
public class MyApplication extends Application {
  @Override
  public Set<Class<?>> getClasses() {
    Set<Class<?>> classes = new HashSet<>();
    classes.add(MyResource.class); // Resource implemented in the previous section
    return classes;
  }

  public static void main(String[] args) {
    UndertowJaxrsServer server = new UndertowJaxrsServer();

    ResteasyDeployment deployment = new ResteasyDeploymentImpl();
    deployment.setApplicationClass(MyApplication.class.getName()); // Application implemented in the previous section
    deployment.setInjectorFactoryClass("org.jboss.resteasy.cdi.CdiInjectorFactory"); // set CDI injector factory

    DeploymentInfo deploymentInfo = server.undertowDeployment(deployment, "/");
    deploymentInfo.setClassLoader(Application.class.getClassLoader());
    deploymentInfo.setDeploymentName("Minimal Undertow RESTeasy and Weld CDI Setup"); // set name of deployment
    deploymentInfo.setContextPath("/");
    deploymentInfo.addListener(Servlets.listener(Listener.class)); // add Weld listener to deployment

    server.deploy(deploymentInfo);

    Undertow.Builder builder = Undertow.builder()
        .addHttpListener(8080, "localhost"); // access the server on http://localhost:8080, note that "localhost" should be "0.0.0.0" if you wish for others in the network to connect.
    server.start(builder);
  }
}