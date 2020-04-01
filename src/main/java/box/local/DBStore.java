/**
 * Package servlets.user for
 *
 * @author Maksim Tiunchik
 */
package box.local;

import net.jcip.annotations.ThreadSafe;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Class DBStore - class for work with PSQL DB during multithreading sessions
 *
 * @author Maksim Tiunchik (senebh@gmail.com)
 * @version 0.1
 * @since 21.03.2020
 */
@ThreadSafe
public class DBStore {
    /**
     * inner logger
     */
    private static final Logger LOG = LogManager.getLogger(DBStore.class.getName());

    /**
     * special version of JDBC for multithreading sessions
     */
    private static final BasicDataSource SOURCE = new BasicDataSource();

    /**
     * singleton for DB
     */
    private static final DBStore BASE = new DBStore();

    /**
     * private constructor to set all properties for connection
     */
    private DBStore() {
        SOURCE.setDriverClassName("org.postgresql.Driver");
        SOURCE.setUrl("jdbc:postgresql://127.0.0.1:5432/box");
        SOURCE.setUsername("postgres");
        SOURCE.setPassword("password");
        SOURCE.setMinIdle(5);
        SOURCE.setMaxIdle(10);
        SOURCE.setMaxOpenPreparedStatements(100);
        createTB();
    }

    /**
     * static method to get link to DBStore
     *
     * @return link to DBStore
     */
    public static DBStore getInstance() {
        return BASE;
    }


    public void addBox(Box box) {
        try (Connection connection = SOURCE.getConnection();
             PreparedStatement st = connection
                     .prepareStatement("INSERT INTO box (ID, CONTAINED_IN) VALUES (?,?)")) {
            st.setInt(1, Integer.parseInt(box.getId()));
            st.setInt(2, Integer.parseInt(box.getParent()));
            st.execute();
        } catch (SQLException e) {
            LOG.error("insert box error", e);
        }
    }

    public void addImage(Item img) {
        try (Connection connection = SOURCE.getConnection();
             PreparedStatement st = connection
                     .prepareStatement("INSERT INTO item (ID, CONTAINED_IN, COLOR) VALUES (?,?,?)")) {
            st.setInt(1, Integer.parseInt(img.getId()));
            st.setInt(2, Integer.parseInt(img.getParent()));
            st.setString(3, img.getColor());
            st.execute();
        } catch (SQLException e) {
            LOG.error("insert image error", e);
        }
    }

    private List<Integer> searcBoxes(int id) {
        List<Integer> answer = new ArrayList<>(20);
        LinkedList<Integer> searchLine = new LinkedList<>();
        searchLine.add(id);
        while (!searchLine.isEmpty()) {
            int number = searchLine.pollFirst();
            answer.add(number);
            try (Connection connection = SOURCE.getConnection();
                 PreparedStatement st = connection
                         .prepareStatement("SELECT ID FROM BOX WHERE CONTAINED_IN = ?")) {
                st.setInt(1, number);
                ResultSet result = st.executeQuery();
                while (result.next()) {
                    searchLine.add(result.getInt("ID"));
                }
            } catch (SQLException e) {
                LOG.error("insert image error", e);
            }
        }
        return answer;
    }

    public List<Integer> searchItems(int id, String color) {
        List<Integer> answer = new ArrayList<>(20);
        LinkedList<Integer> searchLine = new LinkedList<>(searcBoxes(id));
        while (!searchLine.isEmpty()) {
            int number = searchLine.pollFirst();
            try (Connection connection = SOURCE.getConnection();
                 PreparedStatement st = connection
                         .prepareStatement("SELECT ID FROM ITEM WHERE COLOR = ? and CONTAINED_IN = ?")) {
                st.setString(1, color);
                st.setInt(2, number);
                ResultSet result = st.executeQuery();
                while (result.next()) {
                    answer.add(result.getInt("ID"));
                }
            } catch (SQLException e) {
                LOG.error("insert image error", e);
            }
        }
        return answer;
    }


    private void createTB() {
        try (Connection connection = SOURCE.getConnection()) {
            PreparedStatement st = connection
                    .prepareStatement("CREATE TABLE IF NOT EXISTS BOX (ID INTEGER PRIMARY KEY, CONTAINED_IN INTEGER)");
            st.execute();
            st = connection
                    .prepareStatement("CREATE TABLE IF NOT EXISTS  ITEM (ID INTEGER PRIMARY KEY, "
                            + "CONTAINED_IN INTEGER REFERENCES BOX(ID), COLOR VARCHAR(100))");
            st.execute();
            st = connection
                    .prepareStatement("INSERT INTO NOX (ID) VALUES (0) ON CONFLICT DO NOTHING ");
            st.execute();
        } catch (SQLException e) {
            LOG.error("Add method SQL ecxeption", e);
        }
    }

}
