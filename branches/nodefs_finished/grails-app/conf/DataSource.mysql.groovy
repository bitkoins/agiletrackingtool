dataSource {
	pooled = true
}

hibernate {
    cache.use_second_level_cache=true
    cache.use_query_cache=true
    cache.provider_class='net.sf.ehcache.hibernate.EhCacheProvider'
}
// environment specific settings
environments {
	development {
		dataSource {
			driverClassName = "org.hsqldb.jdbcDriver"
			dbCreate = "create-drop" // one of 'create', 'create-drop','update'
			username = "sa"
			password = ""
			url = "jdbc:hsqldb:mem:devDB"
		}
	}
	test {
		dataSource {
			driverClassName = "org.hsqldb.jdbcDriver"
			dbCreate = "update"
			username = "sa"
			password = ""
			url = "jdbc:hsqldb:mem:testDb"
		}
	}
	
	production {
		dataSource {
			dbCreate = "update"
			url = "jdbc:mysql://localhost/agiletracking"
			driverClassName = "com.mysql.jdbc.Driver"
			username = "user"
			password = "password"	
		}
	}
}
