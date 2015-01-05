import com.redis.RedisClient

val r = new RedisClient("localhost", 6379)

r.lpush("list-1", "foo")
r.lpush("list-1", "foo2")
r.lpush("list-1", "foo3")
r.lrange("list-1",0,-1).get
r.keys("*").get.f

