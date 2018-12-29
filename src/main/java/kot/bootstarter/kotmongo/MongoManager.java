package kot.bootstarter.kotmongo;

import java.util.List;
import java.util.Map;

/**
 * @author yangyu
 */
public interface MongoManager extends BaseMongoManager {

    /**
     * 创建对象
     *
     * @param example
     */
    <T> void save(T example);

    /**
     * 创建对象
     */
    void save(Object obj, String collection);

    /**
     * 更新文档
     *
     * @param example
     */
    <T> void update(T example);

    /**
     * 更新文档
     *
     * @param map
     */
    void update(Map<String, Object> map, String collection);

    /**
     * 删除文档
     *
     * @param example
     */
    <T> void delete(T example);

    /**
     * 刪除文档
     *
     * @param collection
     */
    void delete(String collection);

    /**
     * 刪除集合
     *
     * @param collection
     */
    void dropCollection(String collection);

    /**
     * 刪除集合
     *
     * @param example
     */
    <T> void dropCollection(T example);

    /**
     * 查询对象
     *
     * @param example
     * @return T
     */
    <T> T findOne(T example);

    /**
     * 查询集合 返回Map
     *
     * @param collection
     * @return List<Map>
     */
    Map<String, Object> findOne(String collection);

    /**
     * 查询集合
     *
     * @param example
     * @return List<T>
     */
    <T> List<T> findPage(T example);

    /**
     * 查询集合
     *
     * @param example
     * @return List<T>
     */
    <T> List<T> list(T example);

    /**
     * 查询集合 返回Map
     *
     * @param collection
     * @return List<Map>
     */
    List<Map> listMap(String collection);

    /**
     * 查询总数
     *
     * @param example
     * @return long
     */
    <T> long count(T example);

    long count(String collection);

    MongoManager showSql();

}
