
package <%= props.packageComponente %>

import grails.converters.JSON
import grails.rest.*
import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional


class <%= props.nombreComponente %>HistoricoRestController extends BaseHistoricoRestController<<%= props.nombreComponente %>Historico> {


	// Services
	def <%= props.nombreComponente_var %>HistoricoService


    /**
     * Constructor
     */
    <%= props.nombreComponente %>HistoricoRestController() {
        super(<%= props.nombreComponente %>Historico)
    }

    @Override
    def getService() {
        return <%= props.nombreComponente_var %>HistoricoService;
    }


    @Override
    def listHistoricos() {
        return <%= props.nombreComponente_var %>HistoricoService.list<%= props.nombreComponente %>Por<%= props.nombreComponentePadre %>(params.<%= props.nombreComponentePadre_var %>RestId?.toInteger())
    }


}
