package jeffaschenk.commons.container.security.object;

/**
 *
 * Granted Authority
 *
 * @author jeffaschenk@gmail.com
 */
public class GrantedAuthority implements org.springframework.security.core.GrantedAuthority {

    String authority;

    public GrantedAuthority(String authority) {
        this.authority = authority;
    }

    public String getAuthority() {
        return authority;
    }

    public void setGrantedAuthority(String authority) {
        this.authority = authority;
    }

    @Override
    public String toString() {
        return this.authority;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GrantedAuthority)) return false;

        GrantedAuthority that = (GrantedAuthority) o;

        if (authority != null ? !authority.equals(that.authority) : that.authority != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return authority != null ? authority.hashCode() : 0;
    }
}
