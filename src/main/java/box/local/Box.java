/**
 * Package local.box for
 *
 * @author Maksim Tiunchik
 */
package box.local;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Objects;

/**
 * Class Box -
 *
 * @author Maksim Tiunchik (senebh@gmail.com)
 * @version 0.1
 * @since 31.03.2020
 */
public class Box {
    private static final Logger LOG = LogManager.getLogger(Box.class.getName());

    private String parent;

    private String id;

    public Box() {
    }

    public Box(String id) {
        this.id = id;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Box box = (Box) o;
        return Objects.equals(parent, box.parent)
               && id.equals(box.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(parent, id);
    }
}
