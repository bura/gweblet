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

Script
---

View
---

Binding
---
