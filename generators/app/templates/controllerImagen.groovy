
package <%= props.packageComponente %>

class Imagen<%= props.nombreComponente %>RestController extends BaseImagenRestController {

    Imagen<%= props.nombreComponente %>RestController() {
        super()
    }


    def getCarpeta() {
        return "/uploads/<%= props.nombreComponente_var %>/"
    }

}
