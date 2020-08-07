package top.kingworker.blog.utils;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Component
public class AuthUtils {
    @Value("${auth.github.clientId}")
    private String githubClientId;

    @Value("${auth.github.clientSecret}")
    private String githubClientSecret;

    @Value("${auth.qq.clientId}")
    private String qqClientId;

    @Value("${auth.qq.redirectURI}")
    private String qqRedirectURI;

    @Value("${auth.qq.clientSecret}")
    private String qqClientSecret;

    /**
     * 获取Githb的AccessToken
     * @param code
     * @return
     * @throws IOException
     */
    public String getGithubAccessToken(String code) throws IOException {
        OkHttpClient client = new OkHttpClient();
        RequestBody formBody = new FormBody.Builder().
                add("client_id",githubClientId)
                .add("client_secret",githubClientSecret).
                add("code",code).build();
        Request request = new Request.Builder()
                .url("https://github.com/login/oauth/access_token")
                .header("Accept","application/json")
                .post(formBody).build();
        Response response = client.newCall(request).execute();
        String jsonStr = response.body().string();
        JSONObject jsonObject = JSONUtil.parseObj(jsonStr);
        return (String) jsonObject.get("access_token");
    }

    /**
     * 可以获取Github的用户信息
     * @param accessToken
     * @return
     * @throws IOException
     */
    public JSONObject getGithubUserInfo(String accessToken) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://api.github.com/user?access_token="+accessToken)
                .get().build();
        Response response = client.newCall(request).execute();
        String jsonStr = response.body().string();
        JSONObject jsonObject = JSONUtil.parseObj(jsonStr);
        return jsonObject;
    }

    /**
     * 跳转到Github认证页面的Url
     * @return
     */
    public String getGithubAuthUrl(){
        return "https://github.com/login/oauth/authorize?client_id="+githubClientId;
    }

    /**
     * 跳转到QQ认证页面的URL
     * @return
     * @throws UnsupportedEncodingException
     */
    public String getQQAuthUrl() throws UnsupportedEncodingException {
        return "https://graph.qq.com/oauth2.0/authorize?response_type=code" +
                "&client_id=" + qqClientId  +
                "&redirect_uri="+ URLEncoder.encode(qqRedirectURI,"utf-8") +
                "&state=qwerty";
    }

    /**
     * 传一个Code 去获取 AccessToken
     * @param code
     * @return
     * @throws IOException
     */
    public String getQQAccessToken(String code) throws IOException {
        String url = "https://graph.qq.com/oauth2.0/token?grant_type=authorization_code&" +
                "client_id="+qqClientId+
                "&client_secret="+qqClientSecret+
                "&code="+code +
                "&redirect_uri="+URLEncoder.encode(qqRedirectURI,"utf-8") +
                "&fmt=json";
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        Response response = client.newCall(request).execute();
        String jsonStr = response.body().string();
        JSONObject jsonObject = JSONUtil.parseObj(jsonStr);
        System.out.println(jsonObject);
        return (String) jsonObject.get("access_token");
    }

    /**
     * 获取QQ的OpenId
     * @param accessToken
     * @return
     * @throws IOException
     */
    public String getQqOpenId(String accessToken) throws IOException {
        String url = "https://graph.qq.com/oauth2.0/me?" +
                "access_token=" + accessToken +
                "&fmt=json";
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        Response response = client.newCall(request).execute();
        String jsonStr = response.body().string();
        JSONObject jsonObject = JSONUtil.parseObj(jsonStr);
        System.out.println(jsonObject);
        return (String) jsonObject.get("openid");
    }

    /**
     * 给AccessToken 和 OpenId 可以获取用户头像和昵称和是否为黄钻
     * @param accessToken
     * @param openId
     * @return
     * @throws IOException
     */
    public JSONObject getQQUserInfo(String accessToken,String openId) throws IOException {
        String url = "https://graph.qq.com/user/get_user_info?" +
                "access_token=" + accessToken +
                "&oauth_consumer_key=" + qqClientId +
                "&openid=" + openId;
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        Response response = client.newCall(request).execute();
        String jsonStr = response.body().string();
        JSONObject jsonObject = JSONUtil.parseObj(jsonStr);
        return jsonObject;
    }

}
