package memcached;

import net.spy.memcached.AddrUtil;
import net.spy.memcached.ConnectionFactoryBuilder;
import net.spy.memcached.MemcachedClient;

/**
 * spymemcached.jarをビルドパスに通す<br>
 * java -jar hostname:port key valueの形式で実行
 */
public class MemcachedSample {

	public static void main(String[] args) {
		String memcachedAddr = args[0];
		MemcachedClient memcachedClient = null;
		String key = args[1];
		String value = args[2];
		System.out.println("Setting Host:" + memcachedAddr);
		try {
			ConnectionFactoryBuilder memcachedConnectionFactory = new ConnectionFactoryBuilder();
			memcachedConnectionFactory.setProtocol( ConnectionFactoryBuilder.Protocol.BINARY );
			memcachedClient = new MemcachedClient( memcachedConnectionFactory.build(), AddrUtil.getAddresses( memcachedAddr ) );
			memcachedClient.set(key, 0, value);
			System.out.println(memcachedClient.get(key));
		} catch ( Exception e ) {
			e.printStackTrace();
		} finally {
			if (memcachedClient != null) {
				memcachedClient.shutdown();
			}
		}
	}
}

