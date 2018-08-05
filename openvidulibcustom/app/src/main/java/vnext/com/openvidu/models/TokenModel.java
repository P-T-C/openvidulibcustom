package vnext.com.openvidu.models;

public class TokenModel {
    String session;
    String role;
    String data;
public TokenModel(){

}
    public TokenModel(String session, String role, String data) {
        this.session = session;
        this.role = role;
        this.data = data;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
