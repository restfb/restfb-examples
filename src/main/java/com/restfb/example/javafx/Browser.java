/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2017 Mark Allen, Norbert Bartels.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.restfb.example.javafx;

import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Version;
import com.restfb.scope.ScopeBuilder;

import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.layout.Region;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

public class Browser extends Region {

  public static final String SUCCESS_URL = "https://www.facebook.com/connect/login_success.html";
  final WebView browser = new WebView();
  final WebEngine webEngine = browser.getEngine();
  private String code;

  private final String appId;

  private final String appSecret;

  public Browser(String appId, String appSecret) {
    this.appId = appId;
    this.appSecret = appSecret;
    // add the web view to the scene
    getChildren().add(browser);
  }

  public void showLogin() {
    DefaultFacebookClient facebookClient = new DefaultFacebookClient(Version.LATEST);
    ScopeBuilder scopes = new ScopeBuilder();
    String loadUrl = facebookClient.getLoginDialogUrl(appId, SUCCESS_URL, scopes);
    webEngine.load(loadUrl + "&display=popup&response_type=code");
    webEngine.getLoadWorker().stateProperty().addListener(
      (ObservableValue<? extends Worker.State> observable, Worker.State oldValue, Worker.State newValue) -> {
        if (newValue != Worker.State.SUCCEEDED) {
          return;
        }

        String myUrl = webEngine.getLocation();

        if ("https://www.facebook.com/dialog/close".equals(myUrl)) {
          System.out.println("dialog closed");
          System.exit(0);
        }

        if (myUrl.startsWith(SUCCESS_URL)) {
          System.out.println(myUrl);
          int pos = myUrl.indexOf("code=");
          code = myUrl.substring(pos + "code=".length());
          FacebookClient.AccessToken token = facebookClient.obtainUserAccessToken(appId,
                  appSecret, SUCCESS_URL, code);
          System.out.println("Accesstoken: " + token.getAccessToken());
        }

      });
  }

  @Override
  protected void layoutChildren() {
    double w = getWidth();
    double h = getHeight();
    layoutInArea(browser, 0, 0, w, h, 0, HPos.CENTER, VPos.CENTER);
  }

  @Override
  protected double computePrefWidth(double height) {
    return 900;
  }

  @Override
  protected double computePrefHeight(double width) {
    return 600;
  }

}