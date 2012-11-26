[#ftl/]

[#assign title="Hello World"/]
[#assign debug=false/]

[#macro html]
<html>
  <head>
    <title>E*Trade - ${title}</title>
  </head>
  [#nested/]
</html>
[/#macro]

[#macro body tag="h1"]
<body>
  <${tag}>${title}</${tag}>
  <p>
    [#if debug]
      <strong>I'm in debug mode</strong><br/>
    [/#if]
    [#nested/]
  </p>
</body>
[/#macro]

[#function sum one two three]
  [#return one + two + three/]
[/#function]

[#function sumList list]
  [#local sum = 0/]
  [#list list as item]
    [#local sum = sum + item/]
  [/#list]
  [#return sum/]
[/#function]



















