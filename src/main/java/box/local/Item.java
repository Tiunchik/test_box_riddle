/**
 * Package local.box for
 *
 * @author Maksim Tiunchik
 */
package box.local;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
}
