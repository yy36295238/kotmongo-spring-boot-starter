# kotmongo

### 这是一个springboot-starter项目

###### 对MongoTemplate进行封装，spring-boot-starter-data-mongodb
注意：springboot-2.0以上版本


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
                .between("age", 0, 100) //范围查询
                .lte("score1",100) // 小于
                .gt("score1",0) // 大于
                .orderBy("_id").direction(Sort.Direction.DESC) // 排序
                .page(1,10) // 分页
                .fields(Arrays.asList("userName","age","score1","score2")) // 返回指定字段
                .showSql() // 显示执行命令（类似SQL）
                .findPage(new UserInfo("zhangsan156")).forEach(u -> System.out.println(u.toString()));
        
// 更新文档        
kotMongoTemplate.createMangoManager().eq("userName","zhangsan1").update(UserInfo.builder().userName("zhangsan125").build());

// 删除文档
kotMongoTemplate.createMangoManager().eq("userName", "zhangsan2").delete(new UserInfo());

// 删除集合
kotMongoTemplate.createMangoManager().dropCollection(new UserInfo());
```
