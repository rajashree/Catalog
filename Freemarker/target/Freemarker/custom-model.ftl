[#ftl/]
[#list string as c]
${c_index} = ${c}
[/#list]

${string[2]}

[@directive key1="value1" key2="value2"/]

Sum = ${sum(10, 10, 10, 10, 2)}
SumEx = ${sum(10, 10, 10, 10, 2)}