package cz.xrosecky.catha.config;

import com.google.gson.annotations.SerializedName;

public class CathaConfig {
    public static CathaConfig newInstance() {
        return new CathaConfig();
    }

    @SerializedName("ResourcepackName")
    private String ResourcepackName = "Catha Resourcepack";
    public String getResourcepackName() { return ResourcepackName; }

    @SerializedName("ResourcepackPrompt")
    private String ResourcepackPrompt = "Resourcepack is required for the server";
    public String getResourcepackPrompt() { return ResourcepackPrompt; }

    @SerializedName("ServerPort")
    private int ServerPort = 7270;
    public int getServerPort() { return ServerPort; }

    @SerializedName("ServerUrl")
    private String ServerUrl = "http://127.0.0.1:7270/";
    public String getServerUrl() { return ServerUrl; }
}