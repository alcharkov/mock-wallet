package lt.alcharkov.db

import javax.sql.DataSource

class H2 {

    val datasource : DataSource

    constructor() {
        val url = "jdbc:h2:mem:test;TRACE_LEVEL_FILE=4;INIT=RUNSCRIPT from 'init.sql'"
        val datasource = org.h2.jdbcx.JdbcDataSource()
        datasource.setURL(url)

        this.datasource = datasource
    }
}