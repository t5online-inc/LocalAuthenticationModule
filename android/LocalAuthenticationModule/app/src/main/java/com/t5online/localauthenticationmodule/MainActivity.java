package com.t5online.localauthenticationmodule;

import android.os.Bundle;

import com.t5online.nebulacore.Nebula;
import com.t5online.nebulacore.bridge.NebulaActivity;
import com.t5online.nebulacore.bridge.NebulaWebView;
import com.t5online.nebulacore.service.PluginService;

import shared.plugin.LocalAuthenticationPlugin;

public class MainActivity extends NebulaActivity {

    public static final String TARGET_URL = "http://www.t5online.com:9080/nebula/test/localauth.html";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        webView = (NebulaWebView)findViewById(R.id.webView);
        webView.loadUrl(TARGET_URL);
    }

    @Override
    public void loadPlugins() {
        super.loadPlugins();
        PluginService pluginService = (PluginService) Nebula.getService(PluginService.SERVICE_KEY_PLUGIN);
        pluginService.addPlugin("shared.plugin.LocalAuthenticationPlugin", LocalAuthenticationPlugin.PLUGIN_GROUP_LOCAL_AUTHENTICATION);
    }
}
