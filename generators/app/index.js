'use strict';
var Generator = require('yeoman-generator');
var chalk = require('chalk');
var yosay = require('yosay');

var beautify = require('gulp-beautify');



module.exports = Generator.extend({
  

  prompting: function () {
    // Have Yeoman greet the user.
    this.log(yosay(
      'Bienvenido al generador de código ' + chalk.red('Grails')  +'\n Debe estar dentro de la carpeta del proyecto de grails (se debe ver la carpeta grails-app desde donde se corre este programa'
    ));


   function promptProperties(prompt, dest, cb) {
        self.prompt(prompt).then(function (props) {
            if(props.unaPropiedad!= "") {
                dest.properties.push(props.unaPropiedad);
                promptProperties(prompt, dest, cb);
            } else {
               cb();
            }
        });
    }

    var cb = this.async(),
        self = this,
    
     nombreComponente = {
        type: 'input',
        name: 'nombreComponente',
        message: '¿Cual es el nombre del dominio? (Ej: LegajoDigital)',
        default: ''
      },
      nombreURL = {
        type: 'input',
        name: 'nombreURL',
        message: '¿Cual es la última parte de la url? (Ej: fotos)',
        default: ''
      },
      packageComponente = {
        type: 'input',
        name: 'packageComponente',
        message: '¿Cual es el paquete del componente?',
        default: 'ar.com.armada.sipemm',
        store: true
      },
      nombreComponentePadre = {
        type: 'input',
        name: 'nombreComponentePadre',
        message: '¿Cual es el nombre del componente padre? (Solo si el anterior fue verdadero)',
        default: '',
        //store: true
      },
      conImagen = {
        type: 'confirm',
        name: 'conImagen',
        message: '¿Tiene imagen?',
        default: false,
        store: true
      },   
      conHistorico = {
        type: 'confirm',
        name: 'conHistorico',
        message: '¿Tiene historico?',
        default: false,
        store: true
      },
      unaPropiedad = {
        type: 'input',
        name: 'unaPropiedad',
        message: 'Ingrese una propiedad del objeto, Ej: String descripcion / y vacio para para finalizar',
        default: ''
      };



    return this.prompt([nombreComponente, nombreURL, packageComponente, nombreComponentePadre, conImagen, conHistorico]).then(function (props) {
      // To access props later use this.props.someAnswer;
      this.props = props;
      this.props.nombreComponentePadre = this.props.nombreComponentePadre.charAt(0).toUpperCase() + this.props.nombreComponentePadre.slice(1);
      this.props.nombreComponente = this.props.nombreComponente.charAt(0).toUpperCase() + this.props.nombreComponente.slice(1);
      this.props.nombreComponentePadre_var = this.props.nombreComponentePadre.charAt(0).toLowerCase() + this.props.nombreComponentePadre.slice(1);
      this.props.nombreComponente_var = this.props.nombreComponente.charAt(0).toLowerCase() + this.props.nombreComponente.slice(1);
      this.props.properties = [];
      promptProperties(unaPropiedad, this.props, function() {
        //props.properties = answers.properties;
        cb();
      });

    }.bind(this));
  },

  writing: function () {
    //this.registerTransformStream(beautify({indentSize: 2 }));

    /*
    this.fs.copyTpl(
      this.templatePath('dummyfile.txt'),
      this.destinationPath('dummyfile.txt'),
      { props: this.props }
    );
    */
   
    var path = this.props.packageComponente.replace(/\./g, '/');
    path += '/';    

    var _this = this; 

    this.props.properties_domain = '';  
    this.props.properties.forEach(function(prop) {
      _this.props.properties_domain += prop+';\n\t';
    });

    this.props.properties_view_show = '';  
    this.props.properties_view_show_historico = '';  
    this.props.properties_view_index = '';
    this.props.properties.forEach(function(prop) {
      let bEsOtroDomain = true;
      if (prop.indexOf('String')>-1)
        bEsOtroDomain = false;
      if (prop.indexOf('Integer')>-1)
        bEsOtroDomain = false;
      if (prop.indexOf('Long')>-1)
        bEsOtroDomain = false;
      if (prop.indexOf('Char')>-1)
        bEsOtroDomain = false;
      if (prop.indexOf('long')>-1)
        bEsOtroDomain = false;
      if (prop.indexOf('Date')>-1)
        bEsOtroDomain = false;
      if (prop.indexOf('Time')>-1)
        bEsOtroDomain = false;
      
      let partes = prop.split(' ');
      if (bEsOtroDomain) { // Tengo que mostrarlo embebido en el json
        _this.props.properties_view_show += '_'+partes[1]+'  {\n\t\t id: '+_this.props.nombreComponente_var+'.'+partes[1]+'.id\n\t}'; 
        _this.props.properties_view_show_historico += '_'+partes[1]+'  {\n\t\t id: '+_this.props.nombreComponente_var+'Historico.'+partes[1]+'.id\n\t}'; 
        _this.props.properties_view_index += '_'+partes[1]+'  {\n\t\t id: unObjeto.'+partes[1]+'.id,\n\t\t descripcion: unObjeto.'+partes[1]+'.descripcion\n\t}'; 
      }
      else {
        //_this.props.properties_view_show += prop+';\n\t'; // Normal 
        //_this.props.properties_view_show_historico += prop+';\n\t'; // Normal 
        //_this.props.properties_view_index += prop+';\n\t'; // Normal 


        _this.props.properties_view_show += partes[1]+'  \t\t '+_this.props.nombreComponente_var+'.'+partes[1]+'\n\t'; 
        _this.props.properties_view_show_historico += partes[1]+'  \t\t '+_this.props.nombreComponente_var+'Historico.'+partes[1]+'\n\t'; 
        _this.props.properties_view_index += '\t'+partes[1]+'  \t\t unObjeto.'+partes[1]+'\n\t'; 
      }
    });

    this.fs.copyTpl(
      this.templatePath('domain.groovy'),
      this.destinationPath('grails-app/domain/'+path+this.props.nombreComponente+'.groovy'),
      { props: this.props }
    );


    this.fs.copyTpl(
      this.templatePath('service.groovy'),
      this.destinationPath('grails-app/services/'+path+this.props.nombreComponente+'Service.groovy'),
      { props: this.props }
    );

    this.fs.copyTpl(
      this.templatePath('controller.groovy'),
      this.destinationPath('grails-app/controllers/'+path+this.props.nombreComponente+'RestController.groovy'),
      { props: this.props }
    );

    this.fs.copyTpl(
      this.templatePath('index.gson'),
      this.destinationPath('grails-app/views/'+this.props.nombreComponente_var+'Rest/index.gson'),
      { props: this.props }
    )
    this.fs.copyTpl(
      this.templatePath('show.gson'),
      this.destinationPath('grails-app/views/'+this.props.nombreComponente_var+'Rest/show.gson'),
      { props: this.props }
    )

    // Con historico
    if (this.props.conHistorico==true) {

      this.fs.copyTpl(
        this.templatePath('domainHistorico.groovy'),
        this.destinationPath('grails-app/domain/'+path+this.props.nombreComponente+'Historico.groovy'),
        { props: this.props }
      );

      this.fs.copyTpl(
        this.templatePath('serviceHistorico.groovy'),
        this.destinationPath('grails-app/services/'+path+this.props.nombreComponente+'HistoricoService.groovy'),
        { props: this.props }
      );

      this.fs.copyTpl(
        this.templatePath('controllerHistorico.groovy'),
        this.destinationPath('grails-app/controllers/'+path+this.props.nombreComponente+'HistoricoRestController.groovy'),
        { props: this.props }
      );

      this.fs.copyTpl(
        this.templatePath('indexHistorico.gson'),
        this.destinationPath('grails-app/views/'+this.props.nombreComponente_var+'HistoricoRest/index.gson'),
        { props: this.props }
      )
      this.fs.copyTpl(
        this.templatePath('showHistorico.gson'),
        this.destinationPath('grails-app/views/'+this.props.nombreComponente_var+'HistoricoRest/show.gson'),
        { props: this.props }
      )

    }
    // Con imagen
    if (this.props.conImagen==true) {
      this.fs.copyTpl(
        this.templatePath('controllerImagen.groovy'),
        this.destinationPath('grails-app/controllers/'+path+'Imagen'+this.props.nombreComponente+'RestController.groovy'),
        { props: this.props }
      )

      this.fs.copyTpl(
        this.templatePath('saveImagen.gson'),
        this.destinationPath('grails-app/views/Imagen'+this.props.nombreComponente_var+'Rest/save.gson'),
        { props: this.props }
      )
    }

    // Test de integracion
      this.fs.copyTpl(
        this.templatePath('controllerSpec.groovy'),
        this.destinationPath('src/integration-test/groovy/'+path+'functional/'+this.props.nombreComponente+'RestSpec.groovy'),
        { props: this.props }
      )

    // Faltan controllers de imagen, show.gson, index.gson, urlMappings?

    var sentenciaUrlMappings = '"/'+this.props.nombreURL+'"(resources: "'+this.props.nombreComponente_var+'Rest")'
    var sentenciaBorrado = 'MotivoBaja.executeUpdate("delete from '+this.props.nombreComponente+'")';
    if (this.props.conHistorico) {
      sentenciaBorrado += '\nMotivoBaja.executeUpdate("delete from '+this.props.nombreComponente+'Historico")'
      sentenciaUrlMappings += '\n"/'+this.props.nombreURL+'_historico"(resources: "'+this.props.nombreComponente_var+'HistoricoRest")'
    }

    this.log(
     chalk.green('Agregar a UrlMappings:\n'+sentenciaUrlMappings+' \n\n y a InternoService:\n'+sentenciaBorrado)
    );
  },

  install: function () {
    this.installDependencies();
  }
});
