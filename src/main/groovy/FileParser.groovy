// Test

new File('users.csv').splitEachLine('|') {fields ->
    ds.add(
            user_name: fields[0],
            user_id: fields[2],
            email: fields[1]
    )
}

ds.each { println it.user_name }
