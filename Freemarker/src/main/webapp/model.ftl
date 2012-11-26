[#ftl/]
String = ${string}

User

Name = ${user.name}
Age = ${user.age}
Male = ${user.male?string}
Address = ${user.addresses['home'].street} ${user.addresses['home'].city}, ${user.addresses['home'].state}, ${user.addresses['home'].zip}
String Sequence = [#list user.stringSequence as c]${c}[/#list]