
package <%= props.packageComponente %>

import grails.transaction.Transactional

@Transactional
class <%= props.nombreComponente %>Service extends BaseService {


    /**
     * BaseService
     * Constructor (debe ser redefinido en todas las clases que heredan de BaseService)
     * @param resource
     */
    <%= props.nombreComponente %>Service() {
        super(<%= props.nombreComponente %>)
    }


    <% if (props.nombreComponentePadre) { %>
    /**
     * Trae el <%= props.nombreComponente %> (si existe) para el (<%= props.nombreComponentePadre %>.id pasado como parametro
     */
    <%= props.nombreComponente %> get<%= props.nombreComponente %>Por<%= props.nombreComponentePadre %>(<%= props.nombreComponentePadre_var %>_id){
    	return <%= props.nombreComponente %>.get<%= props.nombreComponente %>Por<%= props.nombreComponentePadre %>(<%= props.nombreComponentePadre_var %>_id).get()
    }
    <% } %>

}

