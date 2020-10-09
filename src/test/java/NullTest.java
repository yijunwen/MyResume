import org.junit.Test;

import java.util.Optional;

/**
 * @author yhaj
 * @version 1.0
 * @date 2020/10/9 9:51
 */
public class NullTest {

    @Test
    public void test(){
        Optional<String> optional = Optional.ofNullable("aa");
        System.out.println(optional.isPresent());
    }
}
