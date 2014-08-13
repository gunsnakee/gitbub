import com.meiliwan.emall.commons.exception.JedisClientException;
import com.meiliwan.emall.commons.jedisTool.JedisKey;
import com.meiliwan.emall.commons.jedisTool.ShardJedisTool;
/**
 * Created by wenlepeng on 13-8-20.
 */
public class ResourceAccessTest {

    public static void main(String[] args) throws JedisClientException {
       // ShardJedisTool.getVolatile().hset(JedisKey.imeiliwan$session, "mlw-1378438462851226998", "recharge_form", "123");

        String value = ShardJedisTool.getInstance().hget(JedisKey.session, "mlw-1378438462851226998", "recharge_form");

        System.out.println(value);

        ShardJedisTool.getInstance().hdel(JedisKey.session, "mlw-1378438462851226998", "recharge_form");
    }

}
