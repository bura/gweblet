GWeblet
===

GWeblet it is a simple servlet based on [GroovyScriptEngine][GroovyScriptEngine] and [SimpleTemplateEngine][SimpleTemplateEngine].
[GroovyScriptEngine]: http://groovy.codehaus.org/Embedding+Groovy#EmbeddingGroovy-TheGroovyScriptEngine
[SimpleTemplateEngine]: http://groovy.codehaus.org/Groovy+Templates#GroovyTemplates-SimpleTemplateEngine

Build
---
To build webapp, use

    gradle clean war

Deploy
---
To deploy war file you will need any servlet 3.0 container (for example Tomcat7).

Usage
---
After deployment you can create views and scripts in the root directory of the application. You don't need to restart the container after each changes (view or script). All changes are immediately available.

### Simple Example

Add **script** and **view** into root of application and check `http://{host}:{port}/{appname}` or `http://{host}:{port}/{appname}/index`

index.html
```html
<html>
<head>
    <title>Example</title>
</head>
<body>
    <h1>${greeting}</h1>
    <ol>
    <% sections.each { %>
    	<li>${it}</li>
	<% } %>
	</ol>
</body>
</html>
```

index.groovy
```groovy
String name = params['name'] ?: 'Guest'

greeting = "Hello $name!"

sections = []
(1..10).each {
    sections << "Section $it"
}
```

Also you can use query param: `http://{host}:{port}/{appname}/?name=Andrey`

Script
---

View
---

Binding
---
