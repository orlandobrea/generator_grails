package <%= props.packageComponente %>.functional

import grails.plugins.rest.client.RestResponse
import grails.test.mixin.integration.Integration
import grails.transaction.Rollback


@Integration
@Rollback
class <%= props.nombreComponente %>RestSpec extends BaseFunctionalRestSpec {

def setup() {
        inicializar("paises") // Cualquiera
        inicializar_tablas()
    }

    def cleanup() {
      inicializar_tablas()
    }
    def "Testear <%= props.nombreComponente %>"() {
        setup:
        Long idCreado = 0

        Date fecha = new Date()
        String fechaS = fecha.format("yyyy-MM-dd");

        
        <% if (props.nombreComponentePadre=="LegajoDigital") { %>
        eraseResources("legajos_digitales")
        eraseResources("paises")
        eraseResources("provincias")
        eraseResources("localidades")
        eraseResources("nacionalidades")
        Long provincia_id = postResource([descripcion: UUID.randomUUID().toString()], "provincias").json.id
        Long localidad_id = postResource([descripcion: UUID.randomUUID().toString()], "provincias/${provincia_id}/localidades").json.id
        Long pais_id = postResource([descripcion: UUID.randomUUID().toString(), convenioConArmadaArgentina: true], "paises").json.id
        Long nacionalidad_id = postResource([descripcion: UUID.randomUUID().toString()], "nacionalidades").json.id
        Long tipoDocumento_id = postResource([descripcion: UUID.randomUUID().toString()], "tipos_documentos").json.id



        Long idPadre = postResource([_nacionalidad: [id: nacionalidad_id], _paisEmisor: [id: pais_id], _tipoDocumento: [id: tipoDocumento_id],
                                          apellido     : "apellidoDelLegajo2", celular: "215156156", email: "email2@mail.com", fechaNacimiento: fechaS, habilitado: true,
                                          imagen       : "", nombre: "nombreDelLegago2", numeroDocumento: "232323242", numeroLegajo: "27895552",
                                          numeroLegajoAnterior: "212589", sexo: "M", telefono: "2562123158",
                                          _domicilio: [_localidad: [id: localidad_id], _pais: [id: pais_id],
                                                        calle: "calle 13", ciudadExtranjero: "", departamento: "B", numero: "123", piso: "5",
                                                        provinciaExtranjero: ""]],"legajos_digitales").json.id

        inicializar("legajos_digitales/${idPadre}/<%= props.nombreURL %>")
        <% } else { %>
        /*
        
        eraseResources("xxx")

        Long provincia_id = postResource([descripcion: "1808080prov11101"], "provincias").json.id
        Long localidad_id = postResource([descripcion: "1808080loc11101"], "provincias/${provincia_id}/localidades").json.id
        Long pais_id = postResource([descripcion: "1808080sit11101", convenioConArmadaArgentina: true], "paises").json.id
        Long nacionalidad_id = postResource([descripcion: "nac1"], "nacionalidades").json.id
        Long tipoDocumento_id = postResource([descripcion: "tipoDoc1"], "tipos_documentos").json.id



        Long idPadre = postResource([_nacionalidad: [id: nacionalidad_id], _paisEmisor: [id: pais_id], _tipoDocumento: [id: tipoDocumento_id],
                                          apellido     : "apellidoDelLegajo2", celular: "215156156", email: "email2@mail.com", fechaNacimiento: fechaS, habilitado: true,
                                          imagen       : "", nombre: "nombreDelLegago2", numeroDocumento: "232323242", numeroLegajo: "27895552",
                                          numeroLegajoAnterior: "212589", sexo: "M", telefono: "2562123158",
                                          _domicilio: [_localidad: [id: localidad_id], _pais: [id: pais_id],
                                                        calle: "calle 13", ciudadExtranjero: "", departamento: "B", numero: "123", piso: "5",
                                                        provinciaExtranjero: ""]],"legajos_digitales").json.id
        */
        incializar("<%= props.nombreURL %>")
        <% } %>
        
        when: "Hago un GET /"
        Boolean testCorrecto = testResourceListVacio();

        then: "Espero que no retorne registros"
        true==testCorrecto

        when: "Creo un nuevo objeto"
        <% if (props.nombreComponentePadre) { %>
        RestResponse response = postResource([_<%= props.nombreComponentePadre_var %>: [id: idPadre]
            //, propiedades 
            <% if (props.conImagen) { %> 
            ,urlImagen: "http://eee.com/ert.jpg"
            <% } %>
            ]);
        <% } else { %>
            RestResponse response = postResource([]);
        <% } %>
        idCreado = response.json.id
        then: "Espero el estado 201 (creado)"
        response.status == 201

        when: "Hago una nueva peticion / (GET /)"
        response = getResourceList()
        then: "Espero el registro creado en el punto 2"
        response.status == 200
        response.json.metadata.total == 1
        <% if (props.conImagen) { %> 
        response.json.results[0].urlImagen == "http://eee.com/ert.jpg"
        <% } %>
        response.json.results[0].dateDeleted == null
        response.json.results[0].motivoBaja_id == null

        when: "Hago un GET /${idCreado} espero el objeto creado"
        response = getResource(idCreado)
        then: "Espero que encuentre el registro"
        response.status == 200
        response.json.id == idCreado
        <% if (props.conImagen) { %> 
        response.json.urlImagen == "http://eee.com/ert.jpg"
        <% } %> 

        when: "Actualizo el registro creado PUT /${idCreado}"
        response = putResource(idCreado, [
                                                // propiedades
                                        ])
        then: "Espero que el servidor me indique 200 (ok)"
        response.status == 200

        when: "Hago un GET /${idCreado}"
        response = getResource(idCreado)
        then: "Espero que los datos sean los modificados"
        response.status == 200
        response.json.id == idCreado
        // Validaciones para verificar update
        //response.json.numeroComprobante == "1111111"



        when: "Borrar el recurso PATCH /${idCreado}"
        response = deleteResource(idCreado)
        then: "Debe pasar el test"
        response.status==204

        when: "Hago un GET /${idCreado}"
        response = getResource(idCreado)
        then: "No tiene que retornar ningun objeto porque esta borrado"
        response.status == 404

    }
}
