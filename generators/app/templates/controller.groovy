
package <%= props.packageComponente %>

import grails.converters.JSON
import grails.rest.*
import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional


class <%= props.nombreComponente %>RestController extends BaseRestController<<%= props.nombreComponente %>> {


	// Services
	def <%= props.nombreComponente_var %>Service
	<% if (props.nombreComponentePadre_var) { %>
	def <%= props.nombreComponentePadre_var %>Service
	<% } %>
	<% if (props.conHistorico) { %>
	def <%= props.nombreComponente_var %>HistoricoService
	<% } %>


    /**
     * Constructor
     */
    <%= props.nombreComponente %>RestController() {
        super(<%= props.nombreComponente %>)
    }

    @Override
    def getService() {
        return <%= props.nombreComponente_var %>Service;
    }


    <% if (props.conHistorico) { %>
    @Transactional
    def save() {
        <%= props.nombreComponente %> <%= props.nombreComponente_var %> = <%= props.nombreComponente_var %>Service.get<%= props.nombreComponente %>Por<%= props.nombreComponentePadre %>(params.<%= props.nombreComponentePadre_var %>RestId.toInteger())
        
        if (<%= props.nombreComponente_var %> == null) {
            //  ES LA PRIMERA VEZ QUE SE CARGA UNA FOTO
            if (handleReadOnly()) {
                return
            }
            <%= props.nombreComponente %> obj = createResource()
            obj = procesarObjetoAntesDeGuardar(obj)
            obj.validate()
            if (obj.hasErrors()) {
                transactionStatus.setRollbackOnly()
                println obj.errors
                //respond obj.errors, view:'create' // STATUS CODE 422
                respond obj.errors // STATUS CODE 422
                return
            }

            <%= props.nombreComponente_var %>Service.save(obj)
            def respuesta = [id: obj.id]
            //respond respuesta, status: CREATED
            respond respuesta, status: CREATED
            //respond getShowResponse(obj), status: CREATED

        }
        else {
            if (handleReadOnly()) {
                return
            }
 
            def <%= props.nombreComponente_var %>Historico = new <%= props.nombreComponente %>Historico()

            <%= props.nombreComponente_var %>Historico.properties = <%= props.nombreComponente_var %>.properties
            <%= props.nombreComponente_var %>Historico.id = null

            <%= props.nombreComponente_var %>.properties = getObjectToBind()
            <%= props.nombreComponente_var %>.validate()

            if (<%= props.nombreComponente_var %>.hasErrors()) {
                transactionStatus.setRollbackOnly()
                respond <%= props.nombreComponente_var %>.errors // STATUS CODE 422
                return
            } else {
                <%= props.nombreComponente_var %>HistoricoService.save(<%= props.nombreComponente_var %>Historico)
                <%= props.nombreComponente_var %>Service.save(<%= props.nombreComponente_var %>)
                render status: OK
            }
        }
    }
    <% } %> 

    <% if (props.nombreComponentePadre_var) { %>
    protected <%= props.nombreComponente %> procesarObjetoAntesDeGuardar(<%= props.nombreComponente %> obj) {
        obj.<%= props.nombreComponentePadre_var %> = <%= props.nombreComponentePadre_var %>Service.get(params.<%= props.nombreComponentePadre_var %>RestId.toInteger())
        return obj
    }

    <% } %>


}
