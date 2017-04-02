
package <%= props.packageComponente %>

import grails.transaction.Transactional

@Transactional
class <%= props.nombreComponente %>HistoricoService extends BaseService {


    /**
     * BaseService
     * Constructor (debe ser redefinido en todas las clases que heredan de BaseService)
     * @param resource
     */
    <%= props.nombreComponente %>HistoricoService() {
        super(<%= props.nombreComponente %>Historico)
    }

    <% if (props.nombreComponentePadre) { %>
    /**
     * Trae el <%= props.nombreComponente %> (si existe) para el (<%= props.nombreComponentePadre %>.id pasado como parametro
     */
    List<<%= props.nombreComponente %>Historico> list<%= props.nombreComponente %>Por<%= props.nombreComponentePadre %>(<%= props.nombreComponentePadre_var %>_id){
    	return <%= props.nombreComponente %>Historico.list<%= props.nombreComponente %>Por<%= props.nombreComponentePadre %>(<%= props.nombreComponentePadre_var %>_id).list()
    }
    <% } %>


}


