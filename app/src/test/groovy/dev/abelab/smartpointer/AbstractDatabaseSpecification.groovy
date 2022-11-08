package dev.abelab.smartpointer

import groovy.sql.Sql
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy

import javax.sql.DataSource

/**
 * DBテストの基底クラス
 */
class AbstractDatabaseSpecification extends AbstractSpecification {

    /**
     * SQL Handler
     */
    protected static Sql sql

    @Autowired
    private dataSource(final DataSource dataSource) {
        if (Objects.isNull(sql)) {
            sql = new Sql(new TransactionAwareDataSourceProxy(dataSource))
        }
    }

    /**
     * setup before test class
     */
    def setupSpec() {
        final var timeZone = TimeZone.getTimeZone("Asia/Tokyo")
        TimeZone.setDefault(timeZone)
    }

    /**
     * cleanup after test case
     */
    def cleanup() {
        // DBを初期化するために、テスト終了時にロールバック
        // sql.rollback()

        // GraphQLの統合テストが適切にロールバックされないので、無理矢理DBリセットする
        sql.execute("SET SQL_SAFE_UPDATES = 0")
        sql.execute("DELETE FROM room")
        sql.execute("DELETE FROM user")
        sql.execute("DELETE FROM timer")
        sql.execute("SET SQL_SAFE_UPDATES = 1")
    }

}
