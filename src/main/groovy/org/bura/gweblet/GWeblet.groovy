package org.bura.gweblet

import groovy.servlet.ServletBinding
import groovy.text.SimpleTemplateEngine
import groovy.text.Template
import groovy.text.TemplateEngine
import groovy.transform.CompileStatic

import javax.servlet.ServletConfig
import javax.servlet.ServletException
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.annotation.WebServlet
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@WebServlet(name = 'GWeblet', value = '/*', loadOnStartup = 1)
@CompileStatic
class GWeblet extends HttpServlet {

    private GroovyScriptEngine scriptEngine
    private TemplateEngine viewEngine
    private String rootPath

    @Override
    void init(ServletConfig config) throws ServletException {
        super.init(config)

        rootPath = config.servletContext.getRealPath('/')
        if (!rootPath) {
            rootPath = System.properties['gweblet.rootpath']
        }
        if (!rootPath) {
            throw new ServletException('GWeblet initialization error: rootpath not defined')
        }

        scriptEngine = new GroovyScriptEngine(rootPath)
        viewEngine = new SimpleTemplateEngine()
    }

    public static final String SCRIPT_EXT = '.groovy'
    public static final String VIEW_EXT = '.html'
    public static final String DEFAULT_CONTENT_TYPE = 'text/html'
    public static final String DEFAULT_ENCODING = 'UTF-8'
    public static final String COMMAND_BINDING_NAME = 'command'
    public static final String ROOT_COMMAND = 'index'

    @Override
    void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest) req
        HttpServletResponse response = (HttpServletResponse) res

        // default response settings
        response.contentType = DEFAULT_CONTENT_TYPE
        response.characterEncoding = DEFAULT_ENCODING
        response.status = HttpServletResponse.SC_OK

        // getting command
        String command = request.getPathInfo()
        if (command && command != '/') {
            command = command[1..command.length() - 1]
        } else {
            command = ROOT_COMMAND
        }

        ServletBinding binding = new ServletBinding(request, response, getServletContext())
        binding.variables[COMMAND_BINDING_NAME] = command

        boolean noScript
        String scriptName = command + SCRIPT_EXT
        if (new File(rootPath, scriptName).exists()) { // trying to find a script
            noScript = false
            try {
                scriptEngine.run(scriptName, binding)
            } catch (any) {
                log("Script '${scriptName}' execution error", any)
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR)

                return
            }
        } else {
            noScript = true
        }

        boolean noView
        String viewName = command + VIEW_EXT
        File viewFile = new File(rootPath, viewName)
        if (viewFile.exists()) { // trying to find a view
            noView = false
            Writer out = response.getWriter()
            try {
                Template template = viewEngine.createTemplate(viewFile)
                template.make(binding.getVariables()).writeTo(out)
            } catch (any) {
                log("View '${viewName}' serialization error", any)
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR)

                return
            }
        } else {
            noView = true
        }

        if (noScript && noView) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND)

            return // no routing
        }

        response.flushBuffer()
    }
}
