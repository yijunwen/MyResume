import com.yhaj.xr.util.DBs;
import org.junit.Test;

/**
 * @author yhaj
 * @version 1.0
 * @date 2020/10/14 15:23
 */
public class DBTest {

    @Test
    public void test(){
        String  sql = "INSERT INTO skill(name, level) VALUES ('哈哈', 1) ";
        DBs.getTpl().update(sql);
    }
}
