package jdbc_convention_over_coniguration;

import com.esotericsoftware.yamlbeans.YamlException;
import com.esotericsoftware.yamlbeans.YamlReader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.Map;

/**
 * Created by Iryna on 4/7/15.
 */
public class DbConnector {

    public static void main(String[] args) throws FileNotFoundException, YamlException {
        DbConnector dbConnector = new DbConnector();
        ConnectionInfo connectionInfo = new ConnectionInfo();

        if (args.length != 5) {
            connectionInfo = dbConnector.readConfigFile();
            System.out.println(connectionInfo);
        } else {
            connectionInfo.setHost(args[0]);
            connectionInfo.setPort(args[1]);
            connectionInfo.setDatabase(args[2]);
            connectionInfo.setUser(args[3]);
            connectionInfo.setPassword(args[4]);
            System.out.println(connectionInfo);
        }

        dbConnector.tryToConnect(connectionInfo);
    }

    public ConnectionInfo readConfigFile() throws FileNotFoundException, YamlException {
        ConnectionInfo connInfo = new ConnectionInfo();

        YamlReader reader = new YamlReader(new FileReader(getFileFromResources("database.yaml")));
        Object object = reader.read();
        Map map = (Map)object;
        connInfo.setHost((String) map.get("host"));
        connInfo.setPort((String) map.get("port"));
        connInfo.setDatabase((String) map.get("database"));
        connInfo.setUser((String) map.get("user"));
        connInfo.setPassword((String) map.get("password"));

        return connInfo;
    }

    public File getFileFromResources(String fileName) {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource(fileName).getFile());

        return file;
    }

    public void tryToConnect(ConnectionInfo connectionInfo) {
        String url = "jdbc:mysql://{0}/{1}";
        String userName = connectionInfo.getUser();
        String password = connectionInfo.getPassword();
        url = MessageFormat.format(url, connectionInfo.getHost(), connectionInfo.getDatabase());

        try {
            DriverManager.getConnection(url, userName, password);
            System.out.println("Connection was successful");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
