package com.restfb.example;

import ch.qos.logback.classic.util.ContextInitializer;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Version;
import com.restfb.exception.devicetoken.FacebookDeviceTokenException;
import com.restfb.exception.devicetoken.FacebookDeviceTokenPendingException;
import com.restfb.exception.devicetoken.FacebookDeviceTokenSlowdownException;
import com.restfb.scope.ScopeBuilder;
import com.restfb.types.DeviceCode;
import org.slf4j.bridge.SLF4JBridgeHandler;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.TimeUnit;

public class LoginDeviceAccessExample extends Example {

  private FacebookClient client;

  static {
    System.setProperty(ContextInitializer.CONFIG_FILE_PROPERTY, "logback_info.xml");
  }

  public static void main(String[] args) throws URISyntaxException, IOException {

    if (args.length == 0) {
      throw new IllegalArgumentException(
              "You must provide an app access token parameter. See README for more information.");
    }

    SLF4JBridgeHandler.removeHandlersForRootLogger();
    SLF4JBridgeHandler.install();

    LoginDeviceAccessExample example = new LoginDeviceAccessExample(args[0]);
    DeviceCode deviceCode = example.obtainDeviceCode();
    System.out.println("UserCode: " + deviceCode.getUserCode());
    Desktop.getDesktop().browse(new URI(deviceCode.getVerificationUri()));
    FacebookClient.AccessToken userToken = example.fetchAccessToken(deviceCode);
    if (userToken != null) {
      System.out.println("User Access Token: " + userToken.getAccessToken());
    } else {
      System.out.println("Cannot not acquire an access token");
    }
  }

  LoginDeviceAccessExample(String accessToken) {
    client = new DefaultFacebookClient(accessToken, Version.VERSION_10_0);
  }

  public DeviceCode obtainDeviceCode() {
    return client.fetchDeviceCode(new ScopeBuilder());
  }

  public FacebookClient.AccessToken fetchAccessToken(DeviceCode deviceCode) {
    int timeout = 10; // initial timeout
    FacebookClient.AccessToken token = null;
    int count = 10;
    do {
      System.out.print(count + ": ");
      try {
        token = client.obtainDeviceAccessToken(deviceCode.getCode());
      } catch (FacebookDeviceTokenException e) {
        if (e instanceof FacebookDeviceTokenPendingException) {
          System.out.println("Waiting for User action");
        } else if (e instanceof FacebookDeviceTokenSlowdownException) {
          timeout += 5;
        } else {
          System.out.println(e.getClass());
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
      try {
        TimeUnit.SECONDS.sleep(timeout);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      count--;
    } while (count > 0 && token == null);
    System.out.println();
    return token;
  }
}
