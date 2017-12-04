package finix;

import javax.ws.rs.ApplicationPath;
import java.util.HashSet;
import java.util.Set;

@ApplicationPath("rest")
public class Application extends javax.ws.rs.core.Application {

    public Set<Class<?>> getClasses() {
        return new HashSet<Class<?>>();
    }
}
