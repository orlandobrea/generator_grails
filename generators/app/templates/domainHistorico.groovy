
package <%= props.packageComponente %>

class <%= props.nombreComponente %>Historico {

	<% if (props.nombreComponentePadre) { %>
	<%= props.nombreComponentePadre %> <%= props.nombreComponentePadre_var %>
	<% } %>
	<% if (props.conImagen) { %>
	String urlImagen
	<% } %>
	Date dateCreated
    Date dateDeleted
    UsuarioInterno creadoPor
    UsuarioInterno borradoPor
    MotivoBaja motivoBaja

    static constraints = {
    	<% if (props.nombreComponentePadre) { %>
    	<%= props.nombreComponentePadre_var %> nullable:false, blank:false
    	<% } %>
    	<% if (props.conImagen) { %>
		urlImagen nullable:false, blank:false 
		<% } %>
		dateCreated nullable : true
        dateDeleted nullable : true
        creadoPor nullable : true
        borradoPor nullable : true
        motivoBaja nullable : true

    }

    static namedQueries = {
        filtered NamedQueries.filteredVacio()
        getRegistro NamedQueries.getRegistro()
    }

    <% if (props.nombreComponentePadre) { %>
    static def list<%= props.nombreComponente %>Por<%= props.nombreComponentePadre %>(<%= props.nombreComponentePadre_var %>_id) {
            <%= props.nombreComponente %>.where {
                (<%= props.nombreComponentePadre_var %>.id == <%= props.nombreComponentePadre_var %>_id)
            }
    }
    <% } %>
}