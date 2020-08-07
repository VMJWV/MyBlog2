package top.kingworker.blog.permission;

import org.apache.shiro.authc.UsernamePasswordToken;

public class UserToken extends UsernamePasswordToken {
    public enum LoginType{
        NORMAL,
        FREE
    };
    private LoginType loginType;

    public LoginType getLoginType() {
        return loginType;
    }

    public void setLoginType(LoginType loginType) {
        this.loginType = loginType;
    }

    public UserToken(){}

    public UserToken(final String username,final String pasword,final LoginType loginType){
        super(username,pasword);
        this.loginType = loginType;
    }
}
