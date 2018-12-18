# kotmongo
在MongoTemplate进行封装，spring-boot-starter-data-mongodb

### 这是一个springboot-starter项目

[使用步骤]

1. 将项目打包至本地 mvn clean install 仓库或传入私服

2. 引入jar
```
<dependency>
   <groupId>org.springframework.boot</groupId>
   <artifactId>kotmongo-spring-boot-starter</artifactId>
   <version>1.0.0</version>
</dependency>
```
3. 示例

```java

 @Autowired
 private KotMongoTemplate kotMongoTemplate;

String collection = "userinfo";

// 根据文档名称查询-数量
final long countByCol = kotMongoTemplate.createMangoManager().count(collection);
System.err.println("count by collection: " + countByCol);

// 根据实体对象查询-数量
final long countByBean = kotMongoTemplate.createMangoManager().count(new UserInfo());
System.err.println("count by bean: " + countByBean);

// 根据文档名称查询-对象-Map
final Map map = kotMongoTemplate.createMangoManager().findOne(collection);
System.err.println("findOne by collection: " + map);

// 根据实体对象查询-对象
final UserInfo one = kotMongoTemplate.createMangoManager().findOne(new UserInfo());
System.err.println("findOne by bean: " + one.toString());

// 条件分页查询
kotMongoTemplate.createMangoManager()
        .between("age", 18, 30) // 范围查询
        .orderBy("_id").direction(Sort.Direction.DESC) // 排序
        .page(RandomUtils.nextInt(0, 10), RandomUtils.nextInt(1, 10)) // 分页
        .findPage(new UserInfo())
        .forEach(u-> System.out.println(u.toString()));
        
// 更新文档        
kotMongoTemplate.createMangoManager().eq("userName","zhangsan1").update(UserInfo.builder().userName("zhangsan125").build());

// 删除文档
kotMongoTemplate.createMangoManager().eq("userName", "zhangsan2").delete(new UserInfo());

// 删除集合
kotMongoTemplate.createMangoManager().dropCollection(new UserInfo());
```
