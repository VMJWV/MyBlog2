package top.kingworker.blog.permission;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authz.PermissionsAuthorizationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;


public class MyPermissionAuthFilter extends PermissionsAuthorizationFilter {
    @Override
    public boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws IOException {
        Subject subject = this.getSubject(request,response);
        String[] perms = (String[])((String[]) mappedValue);
        boolean isPermitted = true;
        if (perms != null && perms.length > 0) {
            if (perms.length == 1) {
                if (!isOnePermitted(perms[0],subject)) {
                    isPermitted = false;
                }
            } else {
                if (!subject.isPermittedAll(perms)) {
                    isPermitted = false;
                }
            }
        }
        return isPermitted;
    }

    private boolean isOnePermitted(String perm,Subject subject) {
        String[] permArr = perm.split("\\|");
        for(String p : permArr){
            if(subject.isPermitted(p)){
                return true;
            }
        }
        return false;
    }
}
