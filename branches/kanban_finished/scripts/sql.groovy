def sql = groovy.sql.Sql.newInstance("jdbc:mysql://localhost:3306/agiletracking", "user",
                          "password", "com.mysql.jdbc.Driver")

sql.eachRow("select * from item"){ row ->
       println "${row.description}  - ${row.item_points} - ${row.date_created}"
}

