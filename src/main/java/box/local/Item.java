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
 * Class Image -
 *
 * @author Maksim Tiunchik (senebh@gmail.com)
 * @version 0.1
 * @since 31.03.2020
 */
public class Item {
    private static final Logger LOG = LogManager.getLogger(Item.class.getName());

    private String id;

    private String color;

    private String parent;

    public Item() {
    }

    public Item(String id, String color) {
        this.id = id;
        this.color = color;
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

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Item item = (Item) o;
        return id.equals(item.id)
                && color.equals(item.color)
                && Objects.equals(parent, item.parent);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, color, parent);
    }
}
